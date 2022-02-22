package com.oscarvera.snail.usecases.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.oscarvera.snail.R
import com.oscarvera.snail.model.session.SessionManager
import com.oscarvera.snail.ui.theme.*
import com.oscarvera.snail.usecases.home.*
import com.oscarvera.snail.util.Router

class LoginActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // A surface container using the 'background' color from the theme
            LoginLayout()
        }
    }
}


@Composable
fun LoginLayout() {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val startForResultGoogle =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.let {
                    val credencial = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credencial)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                account.id?.let { id ->
                                    SessionManager.setIdFirebase(id)
                                    db.collection("users").document(id).set(hashMapOf("id" to id))
                                    Router.launchMainActivity(context)
                                }

                            } else {
                                //There is an error with google account
                                //TODO: Show error message
                            }
                        }
                }
            } catch (e: ApiException) {
                //There is an error with Intent or google sign in
                //TODO: Show error message
            }
        }

    val gradient =
        Brush.linearGradient(
            listOf(GradientDark, GradientLight),
            start = Offset(-90.0f, 50.0f),
            end = Offset(50.0f, -250.0f)
        )



    SnailTheme {
        Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxHeight()) {


            Column(
                Modifier
                    .padding(30.dp)
                    .padding(10.dp, 0.dp, 10.dp, 60.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logosnail),
                    contentDescription = "",
                    modifier = Modifier.align(CenterHorizontally).height(140.dp),
                )
                Spacer(modifier = Modifier.height(90.dp))
                GradientButton(
                    gradient = gradient,
                    onClick = {
                        if (SessionManager.isLogged()) {
                            //Is already logged
                            Router.launchMainActivity(context)
                        } else {
                            val googleConf = GoogleSignInOptions
                                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(context.getString(R.string.default_web_client_id))
                                .requestId()
                                .build()
                            val googleClient = GoogleSignIn.getClient(context, googleConf)
                            startForResultGoogle.launch(googleClient.signInIntent)
                        }

                    }, modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    content = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_google_black),
                                contentDescription = "",
                                Modifier.padding(2.dp)
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = context.getString(R.string.login_sign_google),
                                fontSize = 20.sp,
                                color = WhiteText,
                                fontWeight = FontWeight.Black
                            )
                        }

                    }
                )
                Text(
                    text = context.getString(R.string.login_describe_sign_google),
                    fontSize = 16.sp,
                    color = TextColor,
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(0.dp, 5.dp, 0.dp, 0.dp),
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center

                )
                Spacer(modifier = Modifier.height(40.dp))
                Spacer(modifier = Modifier.height(1.dp).background(DisableColor).fillMaxWidth())
                Spacer(modifier = Modifier.height(40.dp))
                OutlinedButton(
                    onClick = {
                        SessionManager.setLocalMode(true)
                        Router.launchMainActivity(context)
                    },
                    border = BorderStroke(1.dp, PrimaryGreen2),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                ) {
                    Text(
                        text = context.getString(R.string.login_sign_local),
                        fontSize = 20.sp,
                        color = PrimaryGreen2,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = context.getString(R.string.login_describe_sign_local),
                    fontSize = 16.sp,
                    color = TextColor,
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(0.dp, 5.dp, 0.dp, 0.dp),
                    fontWeight = FontWeight.Light
                )
            }

        }

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview2() {
    LoginLayout()
}




