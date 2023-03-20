package com.tft.selfbest.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.chat_feature.utils.SharedPrefManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.tft.selfbest.R
import com.tft.selfbest.databinding.ActivityNewLoginBinding
import com.tft.selfbest.models.LogedInData
import com.tft.selfbest.network.NetworkResponse
import com.tft.selfbest.ui.activites.HomeActivity
import com.tft.selfbest.ui.activites.Signup
import com.tft.selfbest.ui.dialog.SignUpProfileDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel by viewModels<LoginViewModel>()

    private lateinit var binding: ActivityNewLoginBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_GET_AUTH_CODE = 9003

    // private lateinit var linkedInRequestManager: LinkedInRequestManager
    private val CLIENT_ID = "78bph43nzzzwa9"
    private val CLIENT_SECRET = "juJb9OUfuz4uZ3rT"
    val REDIRECT_URI = "https://self.best/home"
    private val SCOPE = "r_liteprofile+r_emailaddress"

    val AUTHURL = "https://www.linkedin.com/oauth/v2/authorization"
    //val TOKENURL = "https://www.linkedin.com/oauth/v2/accessToken"
    lateinit var linkedinAuthURLFull: String
    lateinit var linkedIndialog: Dialog
    lateinit var linkedinCode: String
    private var isUserType: Boolean = true
    private var isSignUp: Boolean = false
    val SERVER_CLIENT_ID = "446007361957-vmbmbuivs1r8f0e53q0piridcheo2g8j.apps.googleusercontent.com"

    lateinit var someActivityResultLauncher: ActivityResultLauncher<Intent>
    var isReactivate = false
    var isGoogleLogin = false
    var isLinkedinLogin = false

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        someActivityResultLauncher= registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            Log.e("Google 1", "rqstCode == rcgetAuthCode")
//            if (result.resultCode == Activity.RESULT_OK) {
//                Log.e("Google ", "rqstCode == rcgetAuthCode")
//                val task: Task<GoogleSignInAccount> =
//                    GoogleSignIn.getSignedInAccountFromIntent(result.data)
////            val result =
////                Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
//                handleSignInResult(task)
//                //return
//            }
        //}
        init()
        viewModel.loginApiObserver.observe(this) { response ->
            when (response) {
                is NetworkResponse.Error -> {
                    if(response.msg.equals("Account not active")){
                        val dialogClickListener =
                            DialogInterface.OnClickListener { dialog, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> {
                                        isReactivate = true
                                        if(isLinkedinLogin)
                                            linkedInLogin()
                                        else if(isGoogleLogin)
                                            googleLogin()
                                    }
                                    DialogInterface.BUTTON_NEGATIVE -> {
                                        dialog.dismiss()
                                    }
                                }
                            }

                        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                        builder.setMessage("To reactivate your account click on continue. Reactivating your account will help you to restore your data and account settings").setPositiveButton("Continue", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener).show()

                    }else {
                        Toast.makeText(this, response.msg, Toast.LENGTH_SHORT).show()
                        sharedPrefManager.clear()
                    }
                }
                is NetworkResponse.Loading -> {
                    //  binding.progress.visibility = View.VISIBLE
                }
                is NetworkResponse.Success -> {
                    //  binding.progress.visibility = View.GONE
//                    Log.e("LinkedIn 2", "Success")
//                    Log.e("New User : ","" + response.data!!.isNewUser)

                    if (!response.data!!.isNewUser && response.data.approved == 1) {
                        //Log.e("LinkedIn 3", "Success")
                        viewModel.savedLoginData(response.data)
                        saveUserData(response.data)
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        //Log.e("LinkedIn 3", "Error")
                        if (response.data.isNewUser) {
                            viewModel.savedLoginData(response.data)
                            startActivity(Intent(this, Signup::class.java))
//                            binding.loginScreen.setBackgroundResource(R.drawable.my_gradient)
//                            SignUpProfileDialog().show(
//                                supportFragmentManager,
//                                "SignUpProfileDialog"
//                            )
                        }
                        else
                            Toast.makeText(
                                this,
                                "You are not authorized :-(",
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                }
            }
        }
        // binding.signUp.setOnClickListener(this)
        binding.googleBtn.setOnClickListener(this)
        binding.linkdinBtn.setOnClickListener(this)
        binding.individual.setOnClickListener(this)
        binding.organisation.setOnClickListener(this)
    }

    private fun saveUserData(data: LogedInData) {
        sharedPrefManager.setInt("id",data.id!!)
        sharedPrefManager.setString("token",data.accessToken)
        sharedPrefManager.setString("name",data.fullName)
        sharedPrefManager.setString("refresh_token",data.refreshToken)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.individual -> {
                if (isUserType)
                    return
                isUserType = true
                binding.individual.setBackgroundResource(R.drawable.left_organisation_round)
                    //resources.getDrawable(R.drawable.left_organisation_round)
                binding.individual.setTextColor(Color.WHITE)
                binding.organisation.setBackgroundColor(Color.TRANSPARENT)
                binding.organisation.setTextColor(Color.BLACK)
            }
            R.id.organisation -> {
                if (!isUserType)
                    return
                isUserType = false
                binding.organisation.setBackgroundResource(R.drawable.right_organisation_round)
                    //resources.getDrawable(R.drawable.right_organisation_round)
                binding.organisation.setTextColor(Color.WHITE)
                binding.individual.setBackgroundColor(Color.TRANSPARENT)
                binding.individual.setTextColor(Color.BLACK)
            }
            R.id.sign_up -> {
                SignUpProfileDialog().show(
                    supportFragmentManager,
                    "SignUpProfileDialog"
                )
                isSignUp = !isSignUp
                binding.linkedText.text =
                    if (isSignUp) "SignUp via LinkedIn" else "Connect via Linkedin"
                binding.googleText.text =
                    if (isSignUp) "SignUp via Google" else "Connect via Google"
            }
            R.id.google_btn -> {
                isGoogleLogin = true
                googleLogin()
            }
            R.id.linkdin_btn -> {
                isLinkedinLogin = true
                linkedInLogin()
            }
        }
    }

    private fun linkedInLogin() {
        // linkedInRequestManager.showAuthenticateView(LinkedInRequestManager.MODE_BOTH_OPTIONS)
        setupLinkedinWebviewDialog(linkedinAuthURLFull)
        // setupLinkedinWebviewDialog("https://accounts.google.com/o/oauth2/auth?approval_prompt=force&access_type=offline&client_id=446007361957-eajrsduofmu470dv2rsl0veol35b1bag.apps.googleusercontent.com&redirect_uri=https://self.best/user/profile&response_type=code&scope=https://www.googleapis.com/auth/calendar&state=state-token&flowName=GeneralOAuthFlow")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupLinkedinWebviewDialog(linkedinAuthURLFull: String) {
        linkedIndialog = Dialog(this)
        linkedIndialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        linkedIndialog.setCancelable(true)
        linkedIndialog.setContentView(R.layout.linkedin_popup_layout)
        val window: Window? = linkedIndialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val webView = WebView(this)
        webView.isVerticalScrollBarEnabled = true
        webView.isHorizontalScrollBarEnabled = true
        webView.webViewClient = LinkedInWebViewClient()
        webView.settings.javaScriptEnabled = true
        // webView.settings.userAgentString = "Chrome/56.0.0.0 Mobile"
        webView.loadUrl(linkedinAuthURLFull)
        linkedIndialog.setContentView(webView)
        linkedIndialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_GET_AUTH_CODE) {
            Log.e("Google ", "rqstCode == rcgetAuthCode")
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
//            val result =
//                Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            handleSignInResult(task)
            return
        }
        Log.e("Google ", "rqstCode != rcgetAuthCode")
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun init() {
        //for google login
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(SERVER_CLIENT_ID) //Specifies that an ID token for authenticated users is requested.
                .requestEmail() //to request users email addresses
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        //for linked in
        val state = "linkedin" + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        //linkedinAuthURLFull = "https://www.linkedin.com/uas/oauth2/authorization?response_type=code&client_id=78bph43nzzzwa9&scope=r_liteprofile+r_emailaddress&redirect_uri=https://staging.self.best/home"
        linkedinAuthURLFull =
            "$AUTHURL?response_type=code&client_id=$CLIENT_ID&client_secret=$CLIENT_SECRET&scope=$SCOPE&state=$state&redirect_uri=$REDIRECT_URI"
    }

    //https://www.linkedin.com/uas/oauth2/authorization?response_type=code&client_id=78bph43nzzzwa9&scope=r_liteprofile+r_emailaddress&redirect_uri=https://staging.self.best/home

    private fun googleLogin() {
        //val account = GoogleSignIn.getLastSignedInAccount(this)
        Log.e("Google ", "GoogleLogin()")
        val signInIntent = mGoogleSignInClient.signInIntent
        //someActivityResultLauncher.launch(signInIntent)
        startActivityForResult(signInIntent, RC_GET_AUTH_CODE)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)
            if (account != null) {
                Log.e("Google ", "handleSignInResult")
                if(!isReactivate)
                    viewModel.googleLogin(account, if (isUserType) "Android" else "company")
                else
                    viewModel.googleLogin(account, if (isUserType) "Android" else "company", true)
            }
            Toast.makeText(this, account.email.toString(), Toast.LENGTH_LONG).show()
            mGoogleSignInClient.signOut()
            //  viewModel.googleLogin(account);
            // Logger.d(account)
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    @Suppress("OverridingDeprecatedMember")
    inner class LinkedInWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            view?.clearHistory()
            view?.clearCache(true)
            if (request?.url.toString().startsWith(REDIRECT_URI)) {
                handleUrl(request?.url.toString(), view)
                return true
            }
            return false
        }

        // Check webview url for access token code or error
        private fun handleUrl(url: String, view: WebView?) {
            val uri = Uri.parse(url)
            if (url.contains("code")) {
                linkedinCode = uri.getQueryParameter("code") ?: ""
                Log.e("Linkedin", linkedinCode)
                linkedIndialog.dismiss()
                viewModel.linkedInLogin(linkedinCode, if (isUserType) "Android" else "company", isReactivate)
                //  linkedInRequestForAccessToken()
            } else if (url.contains("error")) {
                Log.e("Linkedin", "Error")
                val error = uri.getQueryParameter("error") ?: ""
                Log.e("Error: ", error)
                linkedIndialog.dismiss()
                Toast.makeText(this@LoginActivity, "Error: $error", Toast.LENGTH_LONG).show()
            }
        }
    }
}