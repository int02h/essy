# Essy
Essy is a simple proxy server that runs [Groovy](https://en.wikipedia.org/wiki/Groovy_(programming_language)) script attached to request so a client is able to do anything it wants with request and its response.

A few things you can do with Essy:
* Modify content of request or response as byte array, plain string or even JSON;
* Modify request/response headers;
* Compress/decompress request/response with gzip;
* Simulate delays and timeouts;
* Measure content length and calculate any statistics.

## How it works
Essy extracts Groovy script from request header named `Essy-Script`. It should be compressed with gzip and encoded with Base64. Then script is executed in special environment with handy methods and specific global variables. Script can do anything it wants and after all must assign global variable that represents the response will be returned to the client.

In fact Groovy script may create completely new response without requesting 'real' server. So proxy became a server itself.

## How to use
1. Run Essy on your local machine or any machine accessible from your network;
2. Change the URL of the request you want to process with Groovy script to the URL of Essy instance (including port);
3. Put compresed and encoded script to the `Essy-Script` header;
4. Perform request and get processed response.