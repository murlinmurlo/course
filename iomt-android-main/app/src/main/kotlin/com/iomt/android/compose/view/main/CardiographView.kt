package com.iomt.android.compose.view.main

import android.widget.SeekBar
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.constraintlayout.widget.ConstraintLayout
import com.iomt.android.EcgShowView
import kotlinx.coroutines.NonDisposableHandle.parent
import java.lang.reflect.Modifier

@Composable
fun EcgShowScreen() {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (onClickConnect, onClickReadDeviceInformation, showDeviceInformation, rateSlider, onClickStartSession, ecgShowView, greetings) = createRefs()

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.constrainAs(onClickConnect) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
        ) {
            Text(text = "Connect")
        }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.constrainAs(onClickReadDeviceInformation) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
        ) {
            Text(text = "Device Information")
        }

        Text(
            text = "Hitler Kaput",
            modifier = Modifier.constrainAs(showDeviceInformation) {
                top.linkTo(onClickReadDeviceInformation.bottom)
                start.linkTo(onClickReadDeviceInformation.start)
            }
        )

        SeekBar(
            onValueChange = { /*TODO*/ },
            value = 0,
            steps = 2,
            modifier = Modifier.constrainAs(rateSlider) {
                top.linkTo(ecgShowView.bottom)
                start.linkTo(ecgShowView.start)
            }
        )

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.constrainAs(onClickStartSession) {
                top.linkTo(rateSlider.bottom)
                start.linkTo(ecgShowView.start)
            }
        ) {
            Text(text = "Start")
        }

        EcgShowView(
            modifier = Modifier.constrainAs(ecgShowView) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        )

        Text(
            text = "Welcome back",
            modifier = Modifier.constrainAs(greetings) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
        )
    }
}
