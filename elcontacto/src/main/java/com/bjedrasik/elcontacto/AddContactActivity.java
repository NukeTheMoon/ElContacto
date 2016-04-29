package com.bjedrasik.elcontacto;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddContactActivity extends Activity {

    private DbAccess _dbAccess;

    private EditText _nameField;
    private EditText _surnameField;
    private EditText _emailField;
    private EditText _phoneField;
    private Button _choosePortraitButton;
    private Button _addContactButton;
    private ImageView _portrait;

    private ContactModel _contactModel;
    private String _filePath;

    private static final int SELECTED_PICTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        _dbAccess = new DbAccess(this);


        _nameField = (EditText)findViewById(R.id.name);
        _surnameField = (EditText)findViewById(R.id.surname);
        _emailField = (EditText)findViewById(R.id.email);
        _phoneField = (EditText)findViewById(R.id.phone);


        _choosePortraitButton = (Button)findViewById(R.id.choosePortraitButton);
        _addContactButton = (Button)findViewById(R.id.addContactButton);

        _portrait = (ImageView)findViewById(R.id.portrait);

        _choosePortraitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECTED_PICTURE);
            }
        });

        _addContactButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        _contactModel = new ContactModel(
                                "",
                                _nameField.getText().toString(),
                                _surnameField.getText().toString(),
                                _emailField.getText().toString(),
                                _phoneField.getText().toString(),
                                getFilePath());

                        String id = _dbAccess.insertData(
                                _contactModel.Name,
                                _contactModel.Surname,
                                _contactModel.Email,
                                _contactModel.Phone,
                                _contactModel.PhotoURL);

                        _contactModel.Id = id;

                        synchronizeWithREST(_contactModel);
                        Intent intent = new Intent(getApplicationContext(), ContactsActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case SELECTED_PICTURE:
                if(resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String filePath = cursor.getString(columnIndex);
                    setFilePath(filePath);
                    cursor.close();

                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                    Drawable drawable = new BitmapDrawable(yourSelectedImage);
                    _portrait.setBackground(drawable);
                }
                break;
            default:
                break;
        }
    }

    public void synchronizeWithREST(ContactModel contact) {

        new HttpHelper() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        }.execute(contact);
    }

    private void setFilePath (String filePath) {
        _filePath = filePath;

    }

    private String getFilePath() {
        return _filePath;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_contact, menu);
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
}
