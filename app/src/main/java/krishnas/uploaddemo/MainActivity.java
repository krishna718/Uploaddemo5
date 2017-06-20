package krishnas.uploaddemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button uploadbn,choosebn;
    private EditText name;
    private ImageView imgView;
    private final  int IMG_REQ = 1;
    private Bitmap bitmap;
    private String UploadUrl="http://192.168.43.97/ImageUploadApp/updateinfo.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadbn=(Button)findViewById(R.id.uploadbn);
        choosebn=(Button)findViewById(R.id.choosebn);
        imgView=(ImageView)findViewById(R.id.imageView);
        name=(EditText)findViewById(R.id.editText);
        choosebn.setOnClickListener(this);
        uploadbn.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.choosebn:

                selectImage();
                break;
            case R.id.uploadbn:
                UploadImage();
                break;
        }
    }


    private void selectImage()
    {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(((requestCode == IMG_REQ) && resultCode == RESULT_OK && (data != null)))
        {
            Uri path=data.getData();
            try{
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imgView.setImageBitmap(bitmap);
                imgView.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    private void UploadImage()
    {
        StringRequest stringRequest =new StringRequest(Request.Method.POST, UploadUrl,
                new Response.Listener<String>()

        {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonobject =new JSONObject(response);
                    String Response=jsonobject.getString("response");
                    Toast.makeText(MainActivity.this,Response,Toast.LENGTH_LONG).show();
                    imgView.setImageResource(0);
                    imgView.setVisibility(View.GONE);
                    name.setText("");
                    name.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String ,String>  getParams() throws AuthFailureError{
                Map<String,String> params=new HashMap<>();
               // ConcurrentHashMap<String, String> params = null;
                params.put("name", name.getText().toString().trim());
                params.put("image",ImageToString(bitmap));
                return params;
            }
        };
        Mysingeton.getInstance(MainActivity.this).addToRequestQue(stringRequest);
    }

    private String ImageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }

}



