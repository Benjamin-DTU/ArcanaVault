import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun SearchBar(
    onSearch: (String) -> Unit
) {
    var userInput by remember { mutableStateOf("") }

    OutlinedTextField(
        value = userInput,
        onValueChange = {
            userInput = it
            onSearch(it) // Call onSearch whenever the input changes
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
        },
        trailingIcon = {
            if (userInput.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear Text",
                    modifier = Modifier.clickable { userInput = "" }
                )
            }
        },
        placeholder = { Text("Search Spells") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}
