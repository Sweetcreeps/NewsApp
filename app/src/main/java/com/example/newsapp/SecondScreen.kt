package com.example.newsapp


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SecondScreen(name:String, navigationToFirstScreen:(String)->Unit) {


    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Text("this is the Second Screen", fontSize = 24.sp)
        Text("Welcome $name", fontSize = 24.sp)
        Button(onClick = {
            navigationToFirstScreen(name)
        }) {
            Text("Go to first Screen")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun SecondPreview() {
    SecondScreen("Bruno",{})
}