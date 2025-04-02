package com.example.newsapp

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// API constants for GNews with your provided key
object ApiConstants {
    const val NEWS_API_KEY = "730a4ea2870ede4f405899aec4dba2f1"
}

// Data model for the news source
data class Source(
    val name: String,
    val url: String?
)

// Data models for the GNews API response
data class GNewsResponse(
    val totalArticles: Int,
    val articles: List<Article>
)

// Updated Article data class with additional fields
data class Article(
    val title: String,
    val description: String?,
    val url: String,
    val image: String?,
    val source: Source?,        // News outlet
    val publishedAt: String?    // Publication date (ISO 8601)
)

// Retrofit API interface for GNews
interface GNewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("topic") topic: String,
        @Query("lang") lang: String = "en",
        @Query("max") max: Int = 10,
        @Query("page") page: Int,
        @Query("token") token: String = ApiConstants.NEWS_API_KEY
    ): GNewsResponse
}

// Singleton object to create the Retrofit service instance for GNews
object GNewsApi {
    private const val BASE_URL = "https://gnews.io/api/v4/"
    val retrofitService: GNewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GNewsApiService::class.java)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FirstScreen() {
    // UI categories with labels
    val categories = listOf("General", "Business", "Sports", "Technology")
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        // Professional header using Material3's TopAppBar
        Header()

        Spacer(modifier = Modifier.height(8.dp))

        // Category Tabs using equal-width buttons with reduced font size
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            categories.forEachIndexed { index, category ->
                Button(
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (pagerState.currentPage == index) Color(0xFF1E88E5) else Color.LightGray
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = category,
                        color = Color.White,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Horizontal Pager for Each Category
        HorizontalPager(state = pagerState) { page ->
            when (categories[page]) {
                "General" -> GeneralPage()
                "Business" -> BusinessPage()
                "Sports" -> SportsPage()
                "Technology" -> TechnologyPage()
                else -> PlaceholderPage(categories[page])
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header() {
    // Create a horizontal gradient for a refined look
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF1E88E5), Color(0xFF42A5F5))
    )
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Swift News Network",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.White
                )
                Text(
                    text = "Your trusted source",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        },
        navigationIcon = {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SNN",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO: Add search functionality */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(gradient)
    )
}

@Composable
fun GeneralPage() {
    Column {
        Text(
            text = "General News",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        // Map "General" to topic "breaking-news"
        NewsListPage(apiCategory = "General")
    }
}

@Composable
fun BusinessPage() {
    Column {
        Text(
            text = "Business News",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        NewsListPage(apiCategory = "Business")
    }
}

@Composable
fun SportsPage() {
    Column {
        Text(
            text = "Sports News",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        NewsListPage(apiCategory = "Sports")
    }
}

@Composable
fun TechnologyPage() {
    Column {
        Text(
            text = "Technology News",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        NewsListPage(apiCategory = "Technology")
    }
}

// Reusable composable that loads news articles for a given API category with infinite scroll
@Composable
fun NewsListPage(apiCategory: String) {
    var articles by remember { mutableStateOf<List<Article>>(emptyList()) }
    var page by remember { mutableStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Map UI category to GNews topic
    val topic = when (apiCategory) {
        "General" -> "breaking-news"
        "Business" -> "business"
        "Sports" -> "sports"
        "Technology" -> "technology"
        else -> "breaking-news"
    }

    LaunchedEffect(page, apiCategory) {
        isLoading = true
        try {
            val response = GNewsApi.retrofitService.getTopHeadlines(
                topic = topic,
                page = page,
                max = 10
            )
            articles = articles + response.articles
        } catch (e: Exception) {
            errorMessage = "Error fetching news: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    errorMessage?.let { msg ->
        Text(
            text = msg,
            color = Color.Red,
            modifier = Modifier.padding(8.dp)
        )
    }

    LazyColumn {
        items(articles) { article ->
            ArticleRow(article = article)
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        if (!isLoading && articles.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                LaunchedEffect(Unit) {
                    page++
                }
            }
        }
    }
}

/**
 * Displays a single article row with:
 * - A thumbnail on the left (resized and cropped)
 * - Title, description, and a row with the source on the left and published date on the right
 * Tapping the row opens the article in a browser.
 */
@Composable
fun ArticleRow(article: Article) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                context.startActivity(intent)
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(article.image)
                .crossfade(true)
                .transformations(RoundedCornersTransformation(4f))
                .build(),
            contentDescription = article.title,
            modifier = Modifier
                .size(100.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = article.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            article.description?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    fontSize = 14.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (article.source != null || article.publishedAt != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    article.source?.let { src ->
                        Text(
                            text = "Source: ${src.name}",
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    }
                    article.publishedAt?.let { published ->
                        Text(
                            text = published.take(10),
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceholderPage(category: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No content for $category yet",
            fontSize = 20.sp
        )
    }
}
