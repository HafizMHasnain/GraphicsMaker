package com.example.graphicsmaker.premium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.graphicsmaker.ui.theme.LogoMakerTheme

class SubscribePremium : ComponentActivity() {
    private val viewModel: SubscriptionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LogoMakerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        SubscriptionScreen(viewModel, this)
                }
            }
        }
    }
}
