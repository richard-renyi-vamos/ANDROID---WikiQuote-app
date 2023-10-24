import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView quoteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quoteTextView = findViewById(R.id.quoteTextView);

        // Fetch a quote and display it
        fetchQuoteAndDisplay();
    }

    private void fetchQuoteAndDisplay() {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://en.wikiquote.org/w/api.php?action=parse&page=Main%20Page&format=json")
                        .build();

                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();

                // Parse the JSON response
                JSONObject json = new JSONObject(jsonData);
                JSONObject parse = json.getJSONObject("parse");
                JSONArray sections = parse.getJSONArray("sections");
                String quote = sections.getJSONObject(0).getString("text");

                // Update the UI with the quote on the main thread
                new Handler(Looper.getMainLooper()).post(() -> {
                    quoteTextView.setText(quote);
                });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
