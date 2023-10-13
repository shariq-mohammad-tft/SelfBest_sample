package com.tft.selfbest.ui.login

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import java.security.SecureRandom
import java.util.Base64
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
//    val REDIRECT_URI = "https://staging.self.best/login"
    private val SCOPE = "r_liteprofile+r_emailaddress"
//    private val MS_SCOPE = arrayOf("https://graph.microsoft.com/email", "https://graph.microsoft.com/offline_access", "https://graph.microsoft.com/openid", "https://graph.microsoft.com/profile", "https://graph.microsoft.com/User.Read")
    private val MS_SCOPE = "openid+profile+email+offline_access+https://graph.microsoft.com/User.Read"
    val AUTHURL = "https://www.linkedin.com/oauth/v2/authorization"

    //val TOKENURL = "https://www.linkedin.com/oauth/v2/accessToken"
    lateinit var linkedinAuthURLFull: String
    lateinit var linkedIndialog: Dialog
    lateinit var linkedinCode: String
    private var isUserType: Boolean = true
    private var isSignUp: Boolean = false
    lateinit var msUrl: String
    val SERVER_CLIENT_ID =
        "446007361957-vmbmbuivs1r8f0e53q0piridcheo2g8j.apps.googleusercontent.com"

    lateinit var someActivityResultLauncher: ActivityResultLauncher<Intent>
    var isReactivate = false
    var isGoogleLogin = false
    var isLinkedinLogin = false
    var isMSlogin = false

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    @RequiresApi(Build.VERSION_CODES.O)
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
                    CookieManager.getInstance().removeAllCookies(null)
                    CookieManager.getInstance().flush()
                    if (response.msg.equals("Account not active")) {
                        val dialogClickListener =
                            DialogInterface.OnClickListener { dialog, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> {
                                        isReactivate = true
                                        if (isLinkedinLogin)
                                            linkedInLogin()
                                        else if (isGoogleLogin)
                                            googleLogin()
                                    }
                                    DialogInterface.BUTTON_NEGATIVE -> {
                                        dialog.dismiss()
                                    }
                                }
                            }

                        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                        builder.setMessage("To reactivate your account click on continue. Reactivating your account will help you to restore your data and account settings")
                            .setPositiveButton("Continue", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener).show()

                    } else {
                        Toast.makeText(this, response.msg, Toast.LENGTH_LONG).show()
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

                    if (response.data!!.redirect.equals("user") && response.data.approved == 1) {
                        //Log.e("LinkedIn 3", "Success")
                        viewModel.savedLoginData(response.data)
                        saveUserData(response.data)
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        //Log.e("LinkedIn 3", "Error")
                        if (response.data.redirect.equals("buildProfile") && response.data.approved == 1) {
                            viewModel.savedLoginData(response.data)
                            saveUserData(response.data)
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("")
                                .setMessage("Your Organisation is Registered with us. Continue to access your organisation account.")
                                .setPositiveButton("Continue") { dialog, which ->
                                    // handle selection
                                    startActivity(Intent(this, Signup::class.java))
                                    dialog.dismiss()

                                }.setNegativeButton("Sign-in with another account"){ dialog, which ->
                                    dialog.dismiss()
                                }
                            val dialog = builder.create()
                            dialog.show()
//                            binding.loginScreen.setBackgroundResource(R.drawable.my_gradient)
//                            SignUpProfileDialog().show(
//                                supportFragmentManager,
//                                "SignUpProfileDialog"
//                            )
                        } else
                            Toast.makeText(
                                this,
                                "You are not authorized :-(",
                                Toast.LENGTH_LONG
                            ).show()
                    }
                }
            }
        }
        // binding.signUp.setOnClickListener(this)
        binding.googleBtn.setOnClickListener(this)
        binding.linkdinBtn.setOnClickListener(this)
        binding.microsoftBtn.setOnClickListener(this)
        binding.individual.setOnClickListener(this)
        binding.organisation.setOnClickListener(this)
    }

    private fun saveUserData(data: LogedInData) {
        sharedPrefManager.setInt("id", data.id!!)
        sharedPrefManager.setString("token", data.accessToken)
        sharedPrefManager.setString("name", data.fullName)
        sharedPrefManager.setString("refresh_token", data.refreshToken)
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
//                binding.googleText.text =
//                    if (isSignUp) "SignUp via Google" else "Connect via Google"
            }
            R.id.google_btn -> {
                isGoogleLogin = true
                isMSlogin = false
                isLinkedinLogin = false
                googleLogin()
            }
            R.id.linkdin_btn -> {
                isLinkedinLogin = true
                isMSlogin = false
                isGoogleLogin = false
                linkedInLogin()
            }
            R.id.microsoft_btn -> {
                isMSlogin = true
                isLinkedinLogin = false
                isGoogleLogin = false
                mslogin()
            }
        }
    }

    private fun linkedInLogin() {
        // linkedInRequestManager.showAuthenticateView(LinkedInRequestManager.MODE_BOTH_OPTIONS)
        setupLinkedinWebviewDialog(linkedinAuthURLFull)
        // setupLinkedinWebviewDialog("https://accounts.google.com/o/oauth2/auth?approval_prompt=force&access_type=offline&client_id=446007361957-eajrsduofmu470dv2rsl0veol35b1bag.apps.googleusercontent.com&redirect_uri=https://self.best/user/profile&response_type=code&scope=https://www.googleapis.com/auth/calendar&state=state-token&flowName=GeneralOAuthFlow")
    }

    private fun mslogin(){
        setupLinkedinWebviewDialog(msUrl)
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

    @RequiresApi(Build.VERSION_CODES.O)
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

//        msUrl = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize?client_id=8bb9b7d4-9c1a-43b8-9b48-3dee851c2aeb&response_type=code&redirect_uri=msauth://com.tft.selfbest/tceN%2Fzcn2DJ3G0fD%2BPtV2o4ycwE%3D&scope=$MS_SCOPE&response_mode=query"

        val nonce = generateNonce(10)
        msUrl = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize?client_id=8bb9b7d4-9c1a-43b8-9b48-3dee851c2aeb&response_type=id_token&redirect_uri=https://self.best/home&scope=$MS_SCOPE&nonce=$nonce&state=12345"
    }

    //https://www.linkedin.com/uas/oauth2/authorization?response_type=code&client_id=78bph43nzzzwa9&scope=r_liteprofile+r_emailaddress&redirect_uri=https://staging.self.best/home

    private fun googleLogin() {
        //val account = GoogleSignIn.getLastSignedInAccount(this)
        Log.e("Google ", "GoogleLogin()")
        val signInIntent = mGoogleSignInClient.signInIntent
        //someActivityResultLauncher.launch(signInIntent)
        startActivityForResult(signInIntent, RC_GET_AUTH_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generateNonce(length: Int): String {
        val random = SecureRandom()
        val nonceBytes = ByteArray(length)
        random.nextBytes(nonceBytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(nonceBytes)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)
            if (account != null) {
                Log.e("Google ", "handleSignInResult")
                if (!isReactivate)
                    viewModel.googleLogin(account, if (isUserType) "Android" else "company")
                else
                    viewModel.googleLogin(account, if (isUserType) "Android" else "company", true)
            }
//            Toast.makeText(this, account.email.toString(), Toast.LENGTH_LONG).show()
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
            if (request?.url.toString().startsWith(REDIRECT_URI) || request?.url.toString().startsWith("msauth://com.tft.selfbest/tceN%2Fzcn2DJ3G0fD%2BPtV2o4ycwE%3D")) {
                handleUrl(request?.url.toString(), view)
                return true
            }
            return false
        }

        // Check webview url for access token code or error
        private fun handleUrl(url: String, view: WebView?) {
            val uri = Uri.parse(url)
            Log.e("Microsoft url", url)
            Log.e("Microsoft uri", uri.toString())
            if (url.contains("code") || url.contains("id_token")) {
                linkedinCode = uri.getQueryParameter("code") ?: ""
//                val msToken = uri.getQueryParameter("id_token") ?: ""
                val msToken = extractIdTokenFromUrl(url) ?: ""
                linkedIndialog.dismiss()
                Log.e("Microsoft", msToken)
                Log.e("Microsoft Linkedin", "$isMSlogin $isLinkedinLogin")
                if(isMSlogin && !isLinkedinLogin){
                    Log.e("Microsoft Login", "1")
                    viewModel.msLogin(
                        msToken,
                        if (isUserType) "Android" else "company",
                        isReactivate
                    )
                }else {
                    Log.e("Linkedin Login", "1")
                    viewModel.linkedInLogin(
                        linkedinCode,
                        if (isUserType) "Android" else "company",
                        isReactivate
                    )
                }
                //  linkedInRequestForAccessToken()
            } else if (url.contains("error")) {
                Log.e("Linkedin", "Error")
                val error = uri.getQueryParameter("error") ?: ""
                Log.e("Microsoft error", error)
                Log.e("Error: ", error)
                linkedIndialog.dismiss()
                Toast.makeText(this@LoginActivity, "Error: $error", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun extractIdTokenFromUrl(urlWithFragment: String): String? {
        val fragment = urlWithFragment.substringAfter('#')
        val params = fragment.split('&')
        for (param in params) {
            val keyValue = param.split('=')
            if (keyValue.size == 2 && keyValue[0] == "id_token") {
                return keyValue[1]
            }
        }
        return null
    }
}