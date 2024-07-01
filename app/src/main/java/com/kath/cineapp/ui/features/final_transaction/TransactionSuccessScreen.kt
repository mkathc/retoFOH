package com.kath.cineapp.ui.features.final_transaction

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kath.cineapp.R

@Composable
fun TransactionSuccessScreen(onTapContinue: () -> Unit) {
    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    onTapContinue()
                },
                modifier = Modifier
                    .fillMaxWidth().padding(20.dp)
                    .wrapContentHeight(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                shape = RoundedCornerShape(30.dp),
                contentPadding = PaddingValues(16.dp),
                enabled = true
            ) {
                Text(
                    text = "Finalizar",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .padding(16.dp)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center) {

            Image(
                painter = painterResource(id = R.drawable.submit_successfully),
                contentDescription = "submit success",
                modifier = Modifier
                    .height(100.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Transacción realizada con éxito",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview
@Composable
fun TransactionSuccessScreenPreview(){
    TransactionSuccessScreen {

    }
}