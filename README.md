<h2 id="udacity-project--popular-movies-2">Udacity Project : Popular Movies 2</h2>
<p>This App reads movie data from MovieDB API, and displays the “Most Popular” movies and “Top Rated” movies inside a <em><strong>Recycler view</strong></em>.</p>
<h2 id="functions">Functions</h2>
<h3 id="sorting">1. Sorting</h3>
<p>The user can sort the list by choosing the criteria. If the user clicks on “POPULAR”, then the list shows the information fetched with “popular” query. In the same way, by clicking “TOP RATED” the list shows the information fetched with “top rated” query.</p>
<h3 id="like-button">2. Like button</h3>
<p>Inside the detail page, the user can click on Like button to save the information into the database. Database was implemented using <em><strong>Room persistance library</strong></em>.</p>
<h3 id="my-favorites-section">3. My Favorites section</h3>
<p>The user can also choose to view which movies they clicked “Like” on. By unclicking the “Like” button, the information is deleted from the database, and is also gone from the favorites list. This immediate UI update was possible because I used <em><strong>ViewModel and Live Data to update the UI</strong></em>.</p>

