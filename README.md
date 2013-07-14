geosearch
=========

This was a project for the university several years ago.

It's written as GWT web app in java and uses the google maps api. The backend queries a PostgreSQL server.

The project is split in three main parts:
- shared: Stuff that's used both by the webapp and by the crawlers
- webapp: The website (frontend and backend)
- crawlers: little programs that crawl location-aware services.

For our university project we wrote three crawlers: Wikipedia articles, Panoramio photos and last.fm events.
We ran the crawlers only once back in 2009 or so (and only for a rectangle that covers Austria), but we ended up
having ~400k objects in the database (mostly panoramio photos).


The website gives you two search boxes, the first one ("Where") to select the map section, the other one ("What")
to filter the results based on tags. The tags are extracted from whatever description we got while crawling
(e.g. the first paragraph of the wiki articles). As we've only covered Austria (with mostly german descriptions),
we simply took all the words that started with a capital letter (and assumed it were german nouns). Then we looked
at the result and filtered out some undesired words (like articles at the start of a sentence, etc.).


One other problem we had was sorting the results. We decided to limit the search results to 50 entries.
Without additional sorting, the database used the lat+lon keys to sort the data (for obvious reasons) and only
returned the 50 most top left results.  
We decided to simply add a random value to each database entry and sort the results based on that (and said that
this may later be replaced by a rating algorithm). That had another neat advantage over a simple `ORDER BY RANDOM()`:
When you zoom in, you get the same results as before (plus some others that weren't in the top50 before).



However, I think the end result is pretty neat, so I decided to put it up here (it was that project that really taught
me how to use database indexes ;) ).

You can look at the end result at [geosearch.fakeroot.at][1].

If you have any questions about this project, contact me either via [github][2] or at manuel (a) reithuber. net.  
Btw.: We were five fourth-semester students at Vienna's University of Technology and we aced that course :)

1: http://geosearch.fakeroot.at/
2: https://github.com/mreithub/
