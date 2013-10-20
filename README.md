StedR (server)
=============

A *Play* application to be run on a *Heroku* instance, or a similar service. One running instance available at http://stedr.herokuapp.com. Provides data using methods described below.

Design
------

There is no database backing up this application. All places are photos retrieved from [flickr](http://www.flickr.com/) and the stories are retrieved from [Digitalt Fortalt](http://digitaltfortalt.no/). For the flickr photo to appear as a place in our application, it needs to be added to [StedR](http://www.flickr.com/groups/2297124@N25/) group on flicker and have the geolocation information filled in.


API Methods
-----------

### Places

Returns all the places available.

    /places.json

### Places in area

Area defined as a rectangle on a map. Assuming a Mercator-projection map like Google Maps are using.
  
    /places_in_area.json?startLatitude=63.397&startLongitude=10.305&stopLatitude=63.454&stopLongitude=10.492

Where `startLatitude` is the the latitude of the west (i.e. left) border of the bounding box, `stopLatitude` is the east (i.e. right) border, `startLongitude` is the south (i.e. bottom) border and `stopLongitude` is the north (i.e. top) border of the bounding box. 

![Map showing bounding box](http://i.imgur.com/GsKoPQZ.png)

All latitudes and longitudes are given in DD ([Decimal Degrees](http://en.wikipedia.org/wiki/Decimal_degrees)). To quote:

> Decimal degrees (DD) express latitude and longitude geographic coordinates as decimal fractions and are used in many geographic information systems (GIS), web mapping applications such as Google Maps, and GPS devices. Decimal degrees are an alternative to using degrees, minutes, and seconds (DMS). As with latitude and longitude, the values are bounded by ±90° and ±180° respectively.
> Positive latitudes are north of the equator, negative latitudes are south of the equator. Positive longitudes are east of Prime Meridian, negative longitudes are west of the Prime Meridian. Latitude and longitude are usually expressed in that sequence, latitude before longitude.

This means **New York City** has a positive latitude and negative longitude, while **Rio de Janeiro** has both values negative and **Tokyo** (or most of the Europe) both values positive.

### Stories

Returns all stories that are in radius of 100 meters from the Place identified by the passed `placeId`.

    http://stedr.herokuapp.com/stories.json?placeId=10357336336
