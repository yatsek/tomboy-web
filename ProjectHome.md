## View Tomboy notes on the web ##

I needed a way to look at my tomboy notes in my mobile phone. I created this web application to publish a tomboy folder (normally ~/.tomboy) or a tomboy sync folder on a web page as a list of notes that can be clicked to view the note.

By publishing a tomboy folder, you publish all notes you have written on the host running the web app. A more interesting option is to publish a sync folder. This way I can write notes on my laptop, sync to the server and have access to them on the bus with the laptop in my backpack.

No authentication implemented yet, so anyone with access to your web server will be able to see your notes.

Written as a java web application that can be installed in any java web app server (tested on tomcat and jetty).