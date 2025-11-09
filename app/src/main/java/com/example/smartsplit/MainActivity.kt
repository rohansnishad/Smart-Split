//package com.example.smartsplit
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import com.example.smartsplit.ui.theme.SmartSplitTheme
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            SmartSplitTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SmartSplitTheme {
//        Greeting("Android")
//    }
//}




//package com.example.smartexpense
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        // Temporary test UI: display a blank text to confirm setup
//        if (savedInstanceState == null) {
//            val textView = android.widget.TextView(this).apply {
//                text = "Smart Expense Splitter is ready!"
//                textSize = 20f
//                setPadding(40, 200, 40, 40)
//            }
//            findViewById<android.view.ViewGroup>(R.id.container).addView(textView)
//        }
//    }
//}

package com.example.smartsplit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_main)
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, com.example.smartsplit.ui.group.GroupListFragment())
                    .commitAllowingStateLoss()
            }
        } catch (ex: Exception) {
            Log.e(TAG, "startup failed", ex)   // you’ll see the full stacktrace in Logcat
            throw ex
        }
    }
}

