package xyz.teamgravity.mokopermissionsdemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import mokopermissionsdemo.composeapp.generated.resources.Res
import mokopermissionsdemo.composeapp.generated.resources.request
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    factory: PermissionsControllerFactory = rememberPermissionsControllerFactory(),
    controller: PermissionsController = remember(factory) { factory.createPermissionsController() },
    viewmodel: AppViewModel = viewModel { AppViewModel(controller) }
) {
    MaterialTheme {
        BindEffect(controller)

        LifecycleEventEffect(
            event = Lifecycle.Event.ON_RESUME,
            onEvent = {
                viewmodel.onCheckPermission()
            }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 10.dp,
                alignment = Alignment.CenterVertically
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = viewmodel.state.name
            )
            Button(
                onClick = viewmodel::onRequestRecordAudioPermission
            ) {
                Text(
                    text = stringResource(Res.string.request)
                )
            }
        }
    }
}