package eu.example.contracker


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.example.contracker.ui.theme.ConTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fun toastMessage(message:String){
            Toast.makeText(this,message, Toast.LENGTH_LONG).show()
        }
        setContent {
            ConTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally){
                        AddContField()
                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContField() {
    var text by remember { mutableStateOf("") }
    var containersList = remember { mutableStateListOf<String>() }
    Column {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 5.dp),
            value = text,
            onValueChange = { text = it.uppercase() },
            label = { Text("Zadej číslo kontejneru") }
        )
        ElevatedButton(onClick = { if (text.isNotBlank())
            containersList.add(text.uppercase());
            text = ""
        }) {
            Text(text = "Přidej")
        }
        @Composable
        fun Container(id: String) {
            Row (
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(all = 10.dp)
            ){
                IconButton(onClick = { containersList.remove(id) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")

                }
                Text(text = id, modifier = Modifier.padding(horizontal = 10.dp))
                OutlinedButton(onClick = {

                }, modifier = Modifier.padding(horizontal = 10.dp)) {
                    Text(text = "Čekni")
                }
                OutlinedButton(onClick = { /*TODO*/ }) {
                    Text(text = "Hlídej")
                }
            }
        }
        LazyColumn() {
            items(items = containersList) { container ->
                Container(id = container.uppercase())
            }
        }
    }
}



