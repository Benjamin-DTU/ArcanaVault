package com.example.arcanavault

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.imageLoader
import com.example.arcanavault.ui.components.ItemView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.system.measureTimeMillis

@RunWith(AndroidJUnit4::class)
class ItemViewPerformanceTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun measureTimeToRenderTenItemViews() {
        // Some sample data for each item
        val sampleDetails = listOf("school: Abjuration", "level: 1", "casting time: 1 action")
        val titles = (1..10).map { "Sample Title $it" }

        val renderTimeMillis = measureTimeMillis {
            composeTestRule.setContent {
                // Retrieve the context and ImageLoader inside the test environment
                val context = LocalContext.current
                val imageLoader = context.imageLoader

                Column {
                    // Render 10 ItemView composables
                    titles.forEach { title ->
                        ItemView(
                            title = title,
                            details = sampleDetails,
                            // Pass the ImageLoader here
                            imageLoader = imageLoader
                        )
                    }
                }
            }
            // Wait for all pending compositions and UI tasks to settle
            composeTestRule.waitForIdle()
        }

    Log.d("ItemViewPerformanceTest", "Rendering 10 ItemView composables took $renderTimeMillis ms")
    }
}