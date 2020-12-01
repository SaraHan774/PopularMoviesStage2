<h2 id="udacity-project--popular-movies-2">Udacity Project : Popular Movies 2</h2>
<p>This App reads movie data from MovieDB API, and displays the “Most Popular” movies and “Top Rated” movies inside a <em><strong>Recycler view</strong></em>.</p>
<h2 id="functions">Functions</h2>
<h3 id="sorting">1. Sorting</h3>
<p>The user can sort the list by choosing the criteria. If the user clicks on “POPULAR”, then the list shows the information fetched with “popular” query. In the same way, by clicking “TOP RATED” the list shows the information fetched with “top rated” query.</p>
<h3 id="like-button">2. Like button</h3>
<p>Inside the detail page, the user can click on Like button to save the information into the database. Database was implemented using <em><strong>Room persistance library</strong></em>.</p>
<h3 id="my-favorites-section">3. My Favorites section</h3>
<p>The user can also choose to view which movies they clicked “Like” on. By unclicking the “Like” button, the information is deleted from the database, and is also gone from the favorites list. This immediate UI update was possible because I used <em><strong>ViewModel and Live Data to update the UI</strong></em>.</p>

---

### What I Learned From This Project 

#### POJO (Plain Old Java Object)
* 프로그래밍을 처음 배울 때 `Java 는 실제 세상의 객체들을 표현하기 적합한 언어` 라 배운다. 
실제 세상의 객체들을 표현하는 단순한 객체들을 Java 언어로 표현한 것이 plain old java object 이다. 
* 가령, `영화` 라는 것의 속성은 다음과 같이 정의할 수 있다. 

```java

public class MovieItem{

    private String mImageUrl;
    private String mMovieName;
    private String mSynopsis;
    private String mReleaseDate;
    private String mVoteAverage;
    private String mPopular;
    private int mMovieID;
// constructors ... 

//getters and setters ...  
}

```

* 영화라는 것을 이미지 URL, 영화 이름, 영화 아이디 와 같은 정보들의 집합체로 단순히 표현한 클래스이다. 
* 생성자와 getter, setter 를 넣어서 객체를 사용할 수 있다. 
* 안드로이드 에서는이러한 custom object 를 Parcelable 이라는 타입으로 취급한다. String, int 와 같은 원시 타입의 
덩어리를 Parcelable 이라는 타입으로 주고 받으면, 프레임웍은 내가 만든 커스텀 객체가 무엇인지 전혀 알 필요 없이 
필요한 연산들을 제공해줄 수 있다. 가령, Intent 로 다른 화면으로 영화의 정보를 넘길 경우 `Intent.putExtra()` 라는 함수에 
Parcelable 을 구현한 POJO 를 전달하면 된다.

<br>

#### HTTP Connection 
* Retrofit 을 사용하지 않고 Java 의 HttpUrlConnection 을 이용해 네트워크 클라이언트를 작성하였다. 
* 이 코드는 GET 요청에 대한 추상화 레이어가 존재하지 않고 굉장히 많은 boilerplate 코드가 존재한다는 단점이 있다.
하지만 HttpUrlConnection 을 활용해서 네트워크 클라이언트를 작성함으로 인해서 Retrofit 내부에서는 대충 이렇게 코드가 
동작하겠구나 하는 것을 배울 수 있었다. 
* 아래는 TMDB 영화 API를 통해서 리뷰를 가져오는 함수이다. 

```java
  static String getReviewsJSON(String movieID){ //param : specific movie id
        HttpsURLConnection urlConnection  = null;
        BufferedReader reader = null;
        String reviewsJSON = null;

        try{
            //URI 를 생성해준다. 
            StringBuffer buffer = new StringBuffer();
            String uriReview = buffer.append(MOVIE_BASE_URL)
                    .append(movieID)
                    .append(QUERY_REVIEWS)
                    .append(Config.MOVIE_API_KEY).toString();
            
            URL urlReview = new URL(Uri.decode(uriReview));

            urlConnection = (HttpsURLConnection) urlReview.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            //요청을 연결한 후 스트림으로 읽어들인다. 
            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();
            String line;
            //버퍼가 비어있지 않은 동안 라인 바이 라인으로 내용들을 읽어들여 
            //StringBuilder 에다가 저장한다. 
            while((line = reader.readLine()) != null){
                builder.append(line);
                builder.append("\n");
                if(builder.length() == 0){
                    return null;
                }
            }
            //리뷰에 대한 JSON 스트링들을 reviewsJSON 이라는 변수에 담아둔다. 
            reviewsJSON = builder.toString();
        }catch (IOException e){
            //IOException 이 일어나면 단순히 stack trace 를 프린트 한다는게 아쉽다. 
            //지금 다시 만들었다면 에러가 일어났을 경우 generic 하게 UI 로 에러 메시지를 
            //뿌려줄 수 있는 구조로 만들었을 것 같다. 
            e.printStackTrace();
        }finally {
            if(urlConnection != null){
            //커넥션을 해제한다. 
                urlConnection.disconnect();
            }
            if(reader != null){
                try{
                //버퍼를 닫아준다. 
                    reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        }
        return reviewsJSON;
    }
```