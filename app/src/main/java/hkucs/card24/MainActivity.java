package hkucs.card24;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    EditText number;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        number = (EditText) findViewById(R.id.target);
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                i.putExtra("number", (String) number.getText().toString());
                startActivity(i);
            }
        });
    }
}
