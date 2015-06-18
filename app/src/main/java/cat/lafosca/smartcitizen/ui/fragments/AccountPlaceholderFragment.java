package cat.lafosca.smartcitizen.ui.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cat.lafosca.smartcitizen.R;
import cat.lafosca.smartcitizen.commons.NonUnderlindeClickableSpan;
import cat.lafosca.smartcitizen.ui.activities.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountPlaceholderFragment extends Fragment {

    private static final int LOGIN_OK = 1001;

    @InjectView(R.id.account_placeholder_info)
    TextView mTextInfo;

    public static AccountPlaceholderFragment newInstance() {
        AccountPlaceholderFragment fragment = new AccountPlaceholderFragment();
        return fragment;
    }

    public AccountPlaceholderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_placeholder, container, false);
        ButterKnife.inject(this, view);

        setSpannableText();

        return view;
    }

    private void setSpannableText() {


        String infoText = getString(R.string.account_placeholder_info);
        String infoTextLink = getString(R.string.account_placeholder_info_link);

        String stringFormatted = infoText +" "+ infoTextLink;

        SpannableString spannableString = SpannableString.valueOf(stringFormatted);

        spannableString.setSpan(
                new ForegroundColorSpan(getResources().getColor(R.color.account_text_2)),
                stringFormatted.length() - infoTextLink.length(), //start
                stringFormatted.length(), //end
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        String url = "http://katborealis.com/wp/wp-content/uploads/2014/09/12702-first-world-problems-template.jpg";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));

        spannableString.setSpan(
                new NonUnderlindeClickableSpan(getActivity(), i),
                stringFormatted.length() - infoTextLink.length(), //start
                stringFormatted.length(), //end
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTextInfo.setText(spannableString, TextView.BufferType.SPANNABLE);
        mTextInfo.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @OnClick(R.id.account_placeholder_login)
    public void login() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);

        startActivityForResult(intent, LOGIN_OK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("activityResult", "requestCode "+requestCode+"\rresultCode "+resultCode);
    }
}
