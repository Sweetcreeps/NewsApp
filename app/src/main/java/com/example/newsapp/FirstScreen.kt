package com.example.newsapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FirstScreen() {
    val categories = listOf("Home", "Politics", "Sports", "Gaming")
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Top Row: Logo & Search Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp, 50.dp)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Logo", fontSize = 18.sp, color = Color.White)
            }
            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.width(200.dp),
                placeholder = { Text("Search") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Category Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            categories.forEachIndexed { index, category ->
                Button(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (pagerState.currentPage == index) Color.Blue else Color.LightGray
                    )
                ) {
                    Text(text = category, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Horizontal Pager for Each Category
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> NewsListPage("Home")
                1 -> NewsListPage("Politics")
                else -> PlaceholderPage(categories[page])
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Footer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "FOOTER", color = Color.White)
        }
    }
}

@Composable
fun NewsListPage(category: String) {
    Column {
        Text(
            text = "Showing: $category News",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        LazyColumn {
            items(4) { _ ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Thumbnail")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "News Title",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun PlaceholderPage(category: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "No content for $category yet", fontSize = 20.sp)
    }
}
//@Preview(showBackground = true)
//@Composable
//fun PreviewFirstScreen() {
//    FirstScreen({})
//}









//@Composable
//fun FirstScreen(navigationToSecondScreen:(String)->Unit) {
//    val name = remember {
//        mutableStateOf("") }
//
//    Column (
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//
//    ){
//        Text("this is the First Screen", fontSize = 24.sp)
//        Spacer(modifier = Modifier.height(16.dp))
//        OutlinedTextField(value = name.value, onValueChange = {
//            name.value = it
//        })
//        Button(onClick = {
//            navigationToSecondScreen(name.value)
//        }) {
//            Text("Go to second Screen")
//        }
//
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun FirstPreview() {
//    FirstScreen({})
//}