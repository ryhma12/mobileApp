package com.example.mobileapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobileapp.R

@Composable
fun HomeScreen(navController: NavController) {
    Box(modifier = Modifier.background(color = Color.Cyan).fillMaxSize()) {
        Box(Modifier.align(Alignment.Center)) {
            Column(verticalArrangement = Arrangement.spacedBy(30.dp)) { //arrangementilla jaetaan molemmat cardit sopivasti näytölle
                Card( //card profiilille, profiilikuvalle, linkkeille ja nimelle
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,

                        ),
                    modifier = Modifier //kortin koko, tarkoitus on tehdä jotakuinkin 2 samankokoista korttia jotka vievät koko ruudun tilan suurinpiirtein
                        .size(width = 350.dp, height = 350.dp)
                ) {

                    Row(horizontalArrangement = Arrangement.spacedBy(50.dp),
                        modifier = Modifier.padding(top = 50.dp, start = 30.dp)) {
                        Image(
                            painter = painterResource(R.drawable.bioplaceholder), //tähän tulee profiilikuva, tällä hetkellä siinä on placeholder
                            contentDescription = null,
                            modifier = Modifier
                                .size(width = 46.dp, height = 46.dp)
                        )
                        Text(
                            "Veikko Vaikuttaja", fontSize = 32.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(top = 200.dp, start = 60.dp)) { //alapuolella on buttonit joihin tulee linkit sosiaalisiin medioihinsa
                        Button(
                            onClick = { /*TODO*/ }, modifier = Modifier //tarkoitus tehdä, että napin kilkkauksella avaa kyseisen somen
                                .size(width = 46.dp, height = 46.dp)
                        ) {
                            Text("X", fontSize = 5.sp) //some joka avataan
                        }
                        Button(
                            onClick = { /*TODO*/ }, modifier = Modifier
                                .size(width = 46.dp, height = 46.dp)
                        ) {
                            Text("twitch")
                        }
                        Button(
                            onClick = { /*TODO*/ }, modifier = Modifier
                                .size(width = 46.dp, height = 46.dp)
                        ) {
                            Text("youtube")
                        }
                        Button(
                            onClick = { /*TODO*/ }, modifier = Modifier
                                .size(width = 46.dp, height = 46.dp)
                        ) {
                            Text("instagram")
                        }
                        Button(
                            onClick = { /*TODO*/ }, modifier = Modifier
                                .size(width = 46.dp, height = 46.dp)
                        ) {
                            Text("tiktok")
                        }
                    }
                }



                Card( //card käyttäjän itse kirjoittamalle biolle
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    modifier = Modifier
                        .size(width = 350.dp, height = 350.dp)
                ) {
                    //tee biosta rullattava ja max 140 merkkiä
                    Text(
                        text = stringResource(R.string.bio), //tähän tulee henkilö itse tekemä bio, tällä hetkellä siinä on placeholder
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.End).padding(10.dp).verticalScroll(rememberScrollState())
                    )

                }
            }
        }
    }

}