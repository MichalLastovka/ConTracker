package eu.example.contracker


import android.os.Bundle
import android.util.Log
import android.widget.Spinner
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
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import eu.example.contracker.ui.theme.ConTrackerTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    var containersList = remember { mutableStateListOf<ContainerItem>() }
    val options = listOf("HHLA", "Eurogate")
    var selectedOption by remember {
        mutableStateOf(options[0])
    }

    Column (horizontalAlignment = Alignment.CenterHorizontally){
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 5.dp),
            value = text,
            onValueChange = { text = it.uppercase() },
            label = { Text("Zadej číslo kontejneru") }
        )
        @Composable
        fun radioCompanies() {

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.Center
            ){
                options.forEach {option->
                    Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 15.dp)){
                        RadioButton(
                            selected = selectedOption == option,
                            onClick = { selectedOption = option })
                        Text(text = option)
                    }
                }
            }
        }
        radioCompanies()
        ElevatedButton(onClick = { if (text.isNotBlank())
            containersList.add(ContainerItem(text.uppercase(), selectedOption));
            text = ""

        }) {
            Text(text = "Přidej")
        }
        @Composable
        fun Container(id: String, company: String) {
            val BASE_URL = "https://coast.hhla.de/"
            val TAG = "CHECK_RESPONSE"
            var status by remember { mutableStateOf("") }

            fun getContainerData(){
                val api = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(myAPI::class.java)
                api.getMyContainer(container = id.uppercase()).enqueue(object : Callback<ContainerData> {
                    override fun onResponse(
                        call: Call<ContainerData>,
                        response: Response<ContainerData>
                    ) {
                        if (response.code() == 200){
                            response.body()?.let {
                                Log.i(TAG, "onResponse: ${response.body()}")
                                val info = response.body()
                                if (info != null) {
                                    status = info.umschlaege[0].reedercode.value.toString()
                                }
                            }
                        }else if (response.code() == 204){
                            println(response.code())
                            status = "Container not found"
                        }
                    }

                    override fun onFailure(call: Call<ContainerData>, t: Throwable) {
                        if (call.isCanceled) {
                            status = "Container not found"
                            println("Container not found")
                            Log.i(TAG, "onFailure: ${t.message}")
                        }
                    }
                })
            }
            Column {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(all = 10.dp)
                ) {
                    IconButton(onClick = { containersList.removeAll{it.id == id} }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")

                    }
                    Text(text = "$id ($company)", modifier = Modifier.padding(horizontal = 10.dp))
                    OutlinedButton(onClick = {
                        getContainerData()
                    }, modifier = Modifier.padding(horizontal = 10.dp)) {
                        Text(text = "Čekni")
                    }
                    OutlinedButton(onClick = { /*TODO*/ }) {
                        Text(text = "Hlídej")
                    }
                }
                Text(text = status)
            }
        }
        LazyColumn() {
            items(items = containersList) { container ->
                Container(id = container.id.toString(), company = container.company.toString())
            }
        }
    }
}



