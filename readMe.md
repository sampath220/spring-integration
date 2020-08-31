#Tracking Service Routing with Spring Integration

>URLs

http://localhost:8080/tracking-proxy/mappings GET
````json
{"mappings":[
    {
        "pattern": ".*/search",
        "mapTo": "https://www.google.com/search"
    },
    {
        "pattern":".*/results",
        "mapTo":"https://www.youtube.com/results"
    }
]
}
````
http://localhost:8080/tracking-proxy/mappings 
- Request Type: POST
- Request payload
````json
{"mappings":[
    {
        "pattern": ".*/search",
        "mapTo": "https://www.google.com/search"
    },
    {
        "pattern":".*/results",
        "mapTo":"https://www.youtube.com/results"
    }
]
}
````
http://localhost:8080/tracking-proxy/mappings 
- Request Type DELETE
- Clears all rules


**Tracking Service Sample URLs**

````http request
http://localhost:8080/tracking-proxy/search?q=spring boot
````
````http request
http://localhost:8080/tracking-proxy/results?q=spring boot
````