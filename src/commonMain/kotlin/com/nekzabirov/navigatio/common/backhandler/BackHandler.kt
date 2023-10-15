package com.nekzabirov.navigatio.common.backhandler

import androidx.compose.runtime.Composable

@Composable
public expect fun BackHandler(enabled: Boolean, onBack: () -> Unit)
