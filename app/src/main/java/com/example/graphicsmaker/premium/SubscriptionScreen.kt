package com.example.graphicsmaker.premium

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.graphicsmaker.R
import com.example.graphicsmaker.ui.theme.colorPrimary
import com.example.graphicsmaker.ui.theme.white
import com.example.graphicsmaker.ui.theme.white1

@Composable
fun SubscriptionScreen(viewModel: SubscriptionViewModel, activity: Activity) {
    val isPremium by viewModel.isPremiumActive.observeAsState(initial = false)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = white)
    ) {
        // Wrap content with Scrollable Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isPremium) {
                PremiumContent()
            } else {
                SubscriptionPrompt(viewModel, activity)
            }
        }
    }
}


@Composable
fun PremiumContent() {
    Text(
        text = "Welcome to Premium!",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(16.dp),
        color = MaterialTheme.colorScheme.primary
    )
    Text(
        text = "Enjoy all the exclusive features you've unlocked.",
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun SubscriptionPrompt(viewModel: SubscriptionViewModel, activity: Activity) {
    val subscriptionPrice by viewModel.subscriptionPrice.observeAsState(initial = "Loading...")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .background(color = white)
            .padding(top = 56.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Unlock Premium Features",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 8.dp),
            color = colorPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        FeatureList()

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(color = white1),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = "Premium Features",
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .background(color = white1),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Start Free Trial (3 Days)",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Display the subscription price dynamically
                Text(
                    text = "Subscription Price: $subscriptionPrice/month",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(
                    text = "Cancel anytime before the trial ends.\nMonthly subscription starts automatically after 3 days.",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = colorPrimary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Button(
                    onClick = {
                        // Trigger the subscription flow
                        viewModel.subscribePremium(activity)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorPrimary,
                        contentColor = white1
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Start Free Trial")
                }
            }
        }
    }
}

@Composable
fun FeatureList() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "What's included:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp),
            color = colorPrimary
        )
        FeatureItem("Ad-free experience")
        FeatureItem("Exclusive content")
        FeatureItem("Priority support")
    }
}

@Composable
fun FeatureItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Feature",
            tint = colorPrimary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge, color = colorPrimary)
    }
}
