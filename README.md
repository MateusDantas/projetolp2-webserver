<h3>
 <a href="http://mateusdantas.github.io/projetolp2-webserver/">Ubet Web Server </a></h3>



<p>Ubet Webserver is a web service created to handle database requests from an Android App 'Ubet' <a href="http://github.com/MateusDantas/projetolp2-app">GitHub</a>. </p>

<p>This webserver is totally implemented in Java using Jersey to receive and process the requests and SQL to manage user data. The project itself is divided into three parts:</p>

<pre><code>- Webservice
- API Communication
- Database handler
</code></pre>

<p>The webservice is implemented using Jersey that receives a user's request, send it to the API Communication that uses the Database Handler to exchange data with the Database itself. All the requests are authenticated using the Auth Token Manager - that you can find on folder "/src/main/java/ubet/sv/" - which provides a safer way to authenticate user's request by not storing its password anywhere besides the database.</p>

<p>Once done with checking the user authentication, the request is sent to the API Communication for processing, all the background operations are done there and then sent to the Database by the Database Handlers.</p>

<p>The Database Handler were programmed to perform SQL Query Statements and all of them are SQL-Injection proof, so the system doesn't permit that malicious users makes forbidden requests do the database, maintaining this way the database secure.</p>

<h3>
<a name="heroku" class="anchor" href="#heroku"><span class="octicon octicon-link"></span></a>Heroku</h3>

<p>The webserver is deployed using Heroku (<a href="http://www.heroku.com">www.heroku.com</a>) and can be done by the following terminal commands:</p>

<p><code>$ git cd webserver</code></p>

<p><code>$ git init</code></p>

<p><code>$ git heroku create</code></p>

<p><code>$ git add src/ pom.xml Procfile system.properties</code></p>

<p><code>$ git commit -a -m {MESSAGE}</code></p>

<p><code>$ git push heroku</code></p>

<p>After deployed the system is ready to go!</p>

<h3>
<a name="games-system-update" class="anchor" href="#games-system-update"><span class="octicon octicon-link"></span></a>Games System Update</h3>

<p>All the games and user's score are automatically updated every 1 hour, so no one has to bother with updating user's score after every game.</p>

<h3>
<a name="authors-and-contributors" class="anchor" href="#authors-and-contributors"><span class="octicon octicon-link"></span></a>Authors and Contributors</h3>

<p>I hereby declare the following contributors, with coding, testing and support for this system:
Gustavo Alves (<a href="https://github.com/GustavoMA" class="user-mention">@GustavoMA</a>)
Bianca 'Rosa' Fook (<a href="https://github.com/biancafook" class="user-mention">@biancafook</a>)
Arysson (<a href="https://github.com/arysson" class="user-mention">@arysson</a>)
And myself of course (<a href="https://github.com/MateusDantas" class="user-mention">@MateusDantas</a>)</p>

<h3>
<a name="support-or-contact" class="anchor" href="#support-or-contact"><span class="octicon octicon-link"></span></a>Support or Contact</h3>

<p>Any troubles with this system, you should add a comment on this page on email us at: mateus.dantas AT ccc.ufcg.edu.br</p>
    
  </body>
</html>
