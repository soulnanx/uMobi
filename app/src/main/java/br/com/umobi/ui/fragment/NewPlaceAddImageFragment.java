package br.com.umobi.ui.fragment;


import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import br.com.umobi.R;
import br.com.umobi.contants.Constants;
import br.com.umobi.contants.ConstantsResultCode;
import br.com.umobi.ui.activity.NewPlaceActivity;
import br.com.umobi.utils.CropImageUtil;
import br.com.umobi.utils.FileUtil;
import br.com.umobi.utils.PermissionsUtils;
import butterknife.BindView;
import butterknife.ButterKnife;


public class NewPlaceAddImageFragment extends Fragment {

    public static final String TAG = "NewPlaceAddImageFragment";

    @BindView(R.id.fragment_new_place_add_image_next)
    Button next;

    @BindView(R.id.fragment_new_place_add_image_button)
    ImageView addPicture;

    @BindView(R.id.fragment_new_place_add_image_picture)
    ImageView picture;

    private View view;
    private NewPlaceActivity newPlaceActivity;
    private byte[] imageBytes;
    private int navigateLoginControl;
    private Uri fileUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_place_add_image, container, false);
        init();
        return view;
    }

    private void init() {
        ButterKnife.bind(this, view);
        newPlaceActivity = ((NewPlaceActivity)NewPlaceAddImageFragment.this.getActivity());
        setEvents();
    }

    private void setEvents() {
        next.setOnClickListener(onClickNext());
        addPicture.setOnClickListener(onClickAddPicture());
        picture.setOnClickListener(onclickPicture());
    }

    private View.OnClickListener onclickPicture() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToButton();
            }
        };
    }

    private View.OnClickListener onClickAddPicture() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionsUtils.hasExternalStoragePermission(NewPlaceAddImageFragment.this.getActivity())) {
                    showDialogAddImage();
                } else {
                    PermissionsUtils.requestExternalStoragePermission(NewPlaceAddImageFragment.this.getActivity());
                }
            }
        };
    }

    private void showDialogAddImage() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NewPlaceAddImageFragment.this.getActivity());
        builder.setItems(R.array.add_photo, onItemAddPhotoClickListener());
        builder.setTitle("Carregar imagem");
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private android.content.DialogInterface.OnClickListener onItemAddPhotoClickListener() {
        return new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        onClickTakePicture();
                        break;
                    case 1:
                        onClickSearchOnGallery();
                        break;

                }
            }

            private void onClickSearchOnGallery() {
                Intent takePictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                ContentValues values = new ContentValues(1);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                fileUri = NewPlaceAddImageFragment.this.getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                startActivityForResult(takePictureIntent, ConstantsResultCode.RESULT_LOAD_IMAGE);
            }

            private void onClickTakePicture() {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(NewPlaceAddImageFragment.this.getActivity().getPackageManager()) != null) {

                    ContentValues values = new ContentValues(1);
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                    fileUri = NewPlaceAddImageFragment.this.getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    startActivityForResult(takePictureIntent, ConstantsResultCode.RESULT_TAKE_IMAGE);
                } else {
                    Toast.makeText(NewPlaceAddImageFragment.this.getActivity(), "não foi possivel receber a imagem", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private View.OnClickListener onClickNext() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPlaceActivity.selectedImage = imageBytes;
                newPlaceActivity.changeFragment(new NewPlaceReviewFragment(), NewPlaceAddImageFragment.TAG);
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                switch (requestCode) {
                    case ConstantsResultCode.RESULT_LOAD_IMAGE:
                        fileUri = data.getData();
                        CropImageUtil.doCrop(NewPlaceAddImageFragment.this, fileUri, 500, 500);
                        break;
                    case ConstantsResultCode.RESULT_TAKE_IMAGE:
                        compressImage();
                        CropImageUtil.doCrop(NewPlaceAddImageFragment.this, fileUri, 500, 500);
                        break;
                    case CropImageUtil.CROP_IMAGE:
                        changeToPicture();
                        cropImage(picture);
                        break;

                }
            } catch (Exception e) {
                Toast.makeText(NewPlaceAddImageFragment.this.getActivity(), "não foi possivel receber a imagem", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changeToPicture() {
        addPicture.setVisibility(View.GONE);
        picture.setVisibility(View.VISIBLE);
    }

    private void changeToButton() {
        picture.setVisibility(View.GONE);
        addPicture.setVisibility(View.VISIBLE);
    }

    private void compressImage() {
        try {
            File image = new File(FileUtil.getPath(NewPlaceAddImageFragment.this.getActivity(), fileUri));
            FileUtil.compressImage(image, Constants.PHOTO_SIZE, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cropImage(ImageView imageView) {
        try {
            File image = new File(FileUtil.getPath(NewPlaceAddImageFragment.this.getActivity(), fileUri));
            Bitmap photo;
            imageBytes = FileUtil.readBytesFromFile(image);
            photo = FileUtil.convertByteArrayToBitmap(FileUtil.readBytesFromFile(image));
            imageView.setImageBitmap(photo);

            if (image.exists()) {
                image.delete();
            }
            fileUri = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
