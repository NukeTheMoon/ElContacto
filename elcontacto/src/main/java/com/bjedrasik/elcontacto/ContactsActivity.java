package com.bjedrasik.elcontacto;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactsActivity extends Activity {

    private DbAccess _dbAccess;
    private FloatingActionButton _addContactButton;
    private String _portraitFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        _addContactButton = (FloatingActionButton)findViewById(R.id.newContact);



        _addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddContactActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _dbAccess = new DbAccess(this);
        LinearLayout databaseContent = (LinearLayout)findViewById(R.id.linearLayout);
        Cursor cursor = _dbAccess.readData();

        int rows = cursor.getCount();
        cursor.moveToFirst();

        for (int i = 0; i < rows; i++) {

            ImageView image = new ImageView(this);
            image.setId(i + 10);
            _portraitFilePath = cursor.getString(5);

            Bitmap yourSelectedImage = BitmapFactory.decodeFile(_portraitFilePath);
            Drawable drawable = new BitmapDrawable(yourSelectedImage);
            image.setBackground(drawable);

            databaseContent.addView(image);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.LEFT;
            params.width = 200;
            params.height = 200;
            image.setLayoutParams(params);

            TextView name = new TextView(this);
            name.setId(i + 100);
            name.setText(cursor.getString(1));
            databaseContent.addView(name);

            TextView surname = new TextView(this);
            surname.setId(i + 1000);
            surname.setText(cursor.getString(2));
            databaseContent.addView(surname);

            TextView email = new TextView(this);
            email.setId(i + 10000);
            email.setText(cursor.getString(3));
            databaseContent.addView(email);

            TextView phone = new TextView(this);
            phone.setId(i + 100000);
            phone.setText(cursor.getString(4));
            databaseContent.addView(phone);





            cursor.moveToNext();


        }
    }
}
