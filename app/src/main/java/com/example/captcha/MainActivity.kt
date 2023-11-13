package com.example.captcha

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.captcha.ui.theme.CaptchaTheme
import com.geetest.captcha.GTCaptcha4Client


class MainActivity : ComponentActivity() {

    private var gtCaptcha4Client: GTCaptcha4Client? = null
    private var validated by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gtCaptcha4Client = GTCaptcha4Client.getClient(this)
        gtCaptcha4Client?.init("7002986629fb242f8d45aa811f2ecc41")

        setContent {
            CaptchaTheme {
                Scaffold(
                    bottomBar = {
                        if (validated.not()) {
                            Button(
                                onClick = { verify() },
                                modifier = Modifier
                                    .padding(20.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(text = "Validate")
                            }
                        }
                    }
                ) { padding ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                    ) {
                        Icon(
                            imageVector = if (validated) Icons.Rounded.Done else Icons.Rounded.Close,
                            contentDescription = null,
                            tint = if (validated) Color.Green else Color.Red,
                            modifier = Modifier.size(200.dp)
                        )
                    }
                }
            }
        }

    }

    private fun verify() {
        gtCaptcha4Client
            ?.addOnSuccessListener { status, response ->
                if (status) {
                    validated = true
                } else {
                    // TODO
                }
            }
            ?.addOnFailureListener { error ->
                Toast.makeText(this, "Validation error", Toast.LENGTH_SHORT).show()
                Log.d("GTCaptcha4Client", "Validation error: $error")
            }
            ?.verifyWithCaptcha()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        gtCaptcha4Client?.configurationChanged(newConfig)
    }

    override fun onDestroy() {
        super.onDestroy()
        gtCaptcha4Client?.destroy()
    }
}