# WikiP
Gson Volley Demo App

About the app functionality
- It send the wikipedia api request and parses the response using volley and Gson library .
- The acquired data fields are populated as a list .
- Each list item should contain an image which is parsed using Picasso Library and appropriate data from the API. 
- Clicking a list item launches the respective Wikipedia page in the webview.
- For UI specification we are using recycler view for smooth and optimise list to give the best use experience.Apart from this 
this search button is provided using which you can do basic search among the list titles .
- Volley and picasso implicitly handles caching of responses to give a good offline experience.
- More emphasis is given on optimization functionality rather than UI . 

Libraries used - 
1)Volley  - For network connection
2)Gson    - For parsing json response
3)Picasso - For loading images from given url
4)Butterknife - For finding view and automatically casting the corresponding view in the layout
5)Recycler view - For optimised rending of items in the list 

Refrences -
1) Api request URL - https://en.wikipedia.org//w/api.php?action=query&format=json&prop=pageimages%7Cpageterms&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpssearch=Sachin+T&gpslimit=10
2) Wikipedia Api - https://www.mediawiki.org/wiki/API:Page_info_in_search_results
3) Wikipedia mobile app - https://play.google.com/store/apps/details?id=org.wikipedia&hl=en
4) https://developer.android.com/training/volley/request-custom.html
5) https://stackoverflow.com/
6) http://jakewharton.github.io/butterknife/
7) http://square.github.io/picasso/
