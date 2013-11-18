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

For getting stories in radius of 100 meters from the Place identified by the passed `placeId`.

    http://stedr.herokuapp.com/stories.json?placeId=10357336336

Returns collection of stories like this:

    [
        {
            "author": "Kulturnett Tr\u00f8ndelag",
            "category": "Arkitektur",
            "fortelling": "\nMed utgangspunkt i Trondheim Kunstmuseum har vi fors\u00f8kt \u00e5 finne ut hva museumsarkitekturen kan fortelle oss om synet p\u00e5 kunst.\n\n",
            "ingress": "Museumsarkitektur kan fortelle s\u00e5 mangt. ",
            "institution": null,
            "language": "Norsk",
            "pictures": [
                "http://media31.dimu.no/media/image/H-DF/DF.1985/4750?height=400&width=400"
            ],
            "tags": [
                "museum",
                "funksjonalisme",
                "kunst",
                "kunstmuseum",
                "bygningshistorie",
                "modernisme",
                "Arkitektur(1)",
                "fortellerkunst",
                "Bildekunst(2)",
                "museumsutstilling",
                "museumsbes\u00f8k",
                "Kultur og samfunn(23)"
            ],
            "title": "Hva forteller museet?",
            "videos": [
                "http://mm01.dimu.no/multimedia/012MtDCo.mp4?mmid=012MtDCo"
            ]
        },
        {
            "author": "Nidaros Domkirkes Restaureringsarbeider",
            "category": "Arkitektur",
            "fortelling": "Videoen ble lagd i forbindelse med prosjektet Nordic Handscape - kulturarv i nye former - i 2005. Prosjektet skulle pr\u00f8ve ut og utvikle nye m\u00e5ter \u00e5 formidle kulturarv p\u00e5 gjennom mobil teknologi.  \nManus: Tove S\u00f8reide/NDR\nProduksjon: Klipp og Lim Media AS\nStemme: Marte Stolp\n",
            "ingress": "Videoen forteller om kalkmalingene i Regalierommet p\u00e5 Erkebispeg\u00e5rden i Trondheim. En f\u00e5r se og h\u00f8re om motivene, om maleren, og hva det hele kostet en gang i tiden. ",
            "institution": null,
            "language": "Norsk",
            "pictures": [
                "http://media31.dimu.no/media/image/H-DF/DF.1682/4136?height=400&width=400",
                "http://media31.dimu.no/media/image/H-DF/DF.1682/4135?height=400&width=400",
                "http://media31.dimu.no/media/image/H-DF/DF.1682/4138?height=400&width=400",
                "http://media31.dimu.no/media/image/H-DF/DF.1682/4137?height=400&width=400"
            ],
            "tags": [
                "nidaros",
                "embetsgardar",
                "Historie og geografi(22)",
                "kunst",
                "borger",
                "Arkitektur(1)",
                "Kulturminne(24)",
                "Bildekunst(2)",
                "kunstnere"
            ],
            "title": "Regalierommet",
            "videos": [
                "http://mm01.dimu.no/multimedia/01JY1q.mp4?mmid=01JY1q"
            ]
        }
    ]


### Images

For getting images for a specified `tag`:

    /images.json?tag=nidaros
    
Returns a collection of stories like this:

    [
        {
            "caption": "S\u00f8ndagstur med @kokkenrolf  #s\u00f8ndagstur #geocaching #nidelva #festningen #trondheim #nidaros #visitnorway #tr\u00f8ndelag",
            "commentCount": 0,
            "fullName": "Tine",
            "likesCount": 14,
            "tags": [
                "visitnorway",
                "geocaching",
                "tr\u00f8ndelag",
                "nidaros",
                "s\u00f8ndagstur",
                "festningen",
                "trondheim",
                "nidelva"
            ],
            "url": "http://distilleryimage11.s3.amazonaws.com/604d77d84faa11e39b501226303f8d71_8.jpg"
        },
        {
            "caption": "Domen st\u00e5r st\u00f8tt etter \u00e5 ha truffet Hilde i g\u00e5r. #trondheim #nidarosdomen #domen #nidaros #h\u00f8st #storm #regn",
            "commentCount": 0,
            "fullName": "Quadrofoglio Adesso",
            "likesCount": 10,
            "tags": [
                "domen",
                "regn",
                "storm",
                "nidaros",
                "h\u00f8st",
                "nidarosdomen",
                "trondheim"
            ],
            "url": "http://distilleryimage8.s3.amazonaws.com/5df117284fa111e3835312819a72a48f_8.jpg"
        }
    ]
