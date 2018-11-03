const SpotifyWebApi = require('spotify-web-api-node');
const spotifyApi = new SpotifyWebApi(require('./config'));
const duplicates = require('array-duplicates');
const helmet = require('helmet');
const _ = require('underscore');
const morgan = require('morgan');

/* Server Trash */
const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const port = process.env.PORT || 3000;

app.use(helmet());
app.use(morgan('combined'));

app.get('/', function (req, res) {
    return res.status(200).send({
        error: false,
        message: "why are you here? lol"
    });
});

let auth = function (callback) {
    spotifyApi.clientCredentialsGrant()
        .then(function (data) {
            spotifyApi.setAccessToken(data.body['access_token']);
            return callback(null, data.body['access_token']);
        }, function (err) {
            return callback(err, null);
        });
}
let findPlaylists = function (query, callback) {

    let tracks = [];
    let trackIDs = [];
    let trackArtists = [];
    let playlistsArr = [];
    let getPlaylistArr = [];

    spotifyApi.searchPlaylists(query, {
        limit: 15,
        offset: 15
    }, (err, data) => {
        if (err) {
            console.log(err)
            return callback(err, null);
        } else {

            /* Create Playlists Array (Light Version) */
            for (let i = 0; i < data.body.playlists.items.length; i++) {
                playlistsArr.push({
                    id: data.body.playlists.items[i].id,
                    name: data.body.playlists.items[i].name,
                    image: data.body.playlists.items[i].images[0].url,
                    author: data.body.playlists.items[i].owner.display_name,
                    trackCount: data.body.playlists.items[i].tracks.total,
                    tracks: []
                });
                getPlaylistArr.push(spotifyApi.getPlaylist(playlistsArr[i].id));
            }

            /* Query all for all the songs */
            Promise.all(getPlaylistArr).then(function (playlists) {
                    for (let x = 0; x < playlists.length; x++) {
                        let songs = playlists[x].body.tracks.items;
                        for (let i = 0; i < songs.length; i++) {
                            if (songs[i].track && songs[i].track.id && songs[i].track.album.images[0]) {
                                tracks.push({
                                    id: songs[i].track.id,
                                    name: songs[i].track.name,
                                    artwork: songs[i].track.album.images[0].url,
                                    artist: songs[i].track.artists[0].name
                                })
                                trackIDs.push(songs[i].track.id);
                                playlistsArr[x].tracks.push(songs[i].track.id);
                            }
                        }

                    }
                    /* Find Duplicates */
                    let dups = duplicates(trackIDs);
                    let resortedObjects = [];
                    let parsedList = [];
                    let mappedDuplicates = dups.reduce(function (prev, cur) {
                        prev[cur] = (prev[cur] || 0) + 1;
                        return prev;
                    }, {});


                    for (var key in mappedDuplicates) {
                        if (mappedDuplicates.hasOwnProperty(key)) {
                            resortedObjects.push({
                                id: key,
                                count: mappedDuplicates[key]
                            })
                        }
                    }

                    resortedObjects = _.sortBy(resortedObjects, 'count');
                    resortedObjects.reverse();

                    for (let i = 0; i < resortedObjects.length; i++) {

                        let parsedSong = _.findWhere(tracks, {
                            id: resortedObjects[i].id
                        })

                        let p = []; /* Find Playlists that have this song */
                        for (let x = 0; x < playlistsArr.length; x++) {
                            if (typeof playlistsArr[x] != 'undefined' && playlistsArr[x].tracks.includes(parsedSong.id)) {
                                let trackDetails = playlistsArr[x];
                                p.push(trackDetails);
                            }
                        }

                        parsedList.push({
                            id: parsedSong.id,
                            name: parsedSong.name,
                            artwork: parsedSong.artwork,
                            artist: parsedSong.artist,
                            dups: p.length,
                            playlists: p
                        })
                    }

                    return callback(null, parsedList);
                })
                .then(function (arrayOfValuesOrErrors) {
                    // TODO
                })
                .catch(function (err) {
                    return callback(err, null);
                });

        }
    });
}

app.get('/fetch', function (req, res) {
    console.log("Ping");
    let query = "halloween";
    if (req.query.s) {
        query = req.query.s;
    }
    auth((err, token) => {
        console.log('token: ' + token);
        if (!err) {
            findPlaylists(query, (err, data) => {
                if (!err) {
                    return res.status(200).send({
                        error: false,
                        data: data
                    });
                } else {
                    console.log(err)
                    return res.status(200).send({
                        error: true,
                        message: err
                    });
                }
            });
        } else {
            console.log('Lmao an error occured');
        }
    });
});

app.listen(port, () => {
    console.log('App us running on port ' + port);
});