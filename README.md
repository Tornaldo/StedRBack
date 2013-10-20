StedR (server)
=============

A *Play* application to be run on a *Heroku* instance, or a similar service. One running instance available at http://stedr.herokuapp.com. Provides data using methods described below.

Design
------

There is no database backing up this application. All places are photos retrieved from [flickr](http://www.flickr.com/) and the stories are retrieved from [Digitalt Fortalt](http://digitaltfortalt.no/). For the flickr photo to appear as a place in our application, it needs to be added to [StedR](http://www.flickr.com/groups/2297124@N25/) group on flicker and have the geolocation information filled in.


API Methods
-----------

All API methods are returning data in JSON format.

### Places

For retrieving all places available.

    /places.json

Returns a collection of places like this:

    [
      {
        "id": "10365419324",
        "owner": "30804868@N05",
        "ownerName": "Simon Stastny",
        "title": "Atlanterhavsveien",
        "latitude": 63.016799,
        "longitude": 7.354445,
        "dateAdded": 1382204661000,
        "pictureUrl": "http:\/\/farm4.staticflickr.com\/3665\/10365419324_698b0c5132.jpg",
        "thumbnailUrl": "http:\/\/farm4.staticflickr.com\/3665\/10365419324_698b0c5132_t.jpg"
      },
      {
        "id": "10365389335",
        "owner": "30804868@N05",
        "ownerName": "Simon Stastny",
        "title": "Rotsethornet",
        "latitude": 62.133544,
        "longitude": 6.095094,
        "dateAdded": 1382204661000,
        "pictureUrl": "http:\/\/farm6.staticflickr.com\/5538\/10365389335_6bd2a47d6b.jpg",
        "thumbnailUrl": "http:\/\/farm6.staticflickr.com\/5538\/10365389335_6bd2a47d6b_t.jpg"
      }
    ]

### Places in area

Returns a collection of places (structure same as above) in an area, where area defined as a rectangle on a map. Assuming a Mercator-projection map like Google Maps are using.
  
    /places_in_area.json?startLatitude=63.397&startLongitude=10.305&stopLatitude=63.454&stopLongitude=10.492

Where `startLongitude` is the the latitude of the west (i.e. left) border of the bounding box, `stopLongitude` is the east (i.e. right) border, `startLatitude` is the south (i.e. bottom) border and `stopLatitude` is the north (i.e. top) border of the bounding box. 

![Map showing bounding box](http://i.imgur.com/GsKoPQZ.png)

All latitudes and longitudes are given in DD ([Decimal Degrees](http://en.wikipedia.org/wiki/Decimal_degrees)). To quote:

> Decimal degrees (DD) express latitude and longitude geographic coordinates as decimal fractions and are used in many geographic information systems (GIS), web mapping applications such as Google Maps, and GPS devices. Decimal degrees are an alternative to using degrees, minutes, and seconds (DMS). As with latitude and longitude, the values are bounded by ±90° and ±180° respectively.
> Positive latitudes are north of the equator, negative latitudes are south of the equator. Positive longitudes are east of Prime Meridian, negative longitudes are west of the Prime Meridian. Latitude and longitude are usually expressed in that sequence, latitude before longitude.

This means **New York City** has a positive latitude and negative longitude, while **Rio de Janeiro** has both values negative and **Tokyo** (or most of the Europe) both values positive.

### Stories

Returns all stories that are in radius of 100 meters from the Place identified by the passed `placeId`.

    http://stedr.herokuapp.com/stories.json?placeId=10357336336
