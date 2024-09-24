package com.github.yohannestz.daystill.ui.base.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll

@ExperimentalMaterial3Api
@Composable
fun PullToRefreshBox(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    state: PullToRefreshState = rememberPullToRefreshState(),
    indicator: @Composable (PullToRefreshState) -> Unit = { pullRefreshState ->
        Indicator(state = pullRefreshState)
    },
    content: @Composable (BoxScope.() -> Unit),
) {
    LaunchedEffect(state.isRefreshing) {
        if (state.isRefreshing) onRefresh()
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) state.startRefresh()
        else state.endRefresh()
    }

    Box(
        modifier = modifier
            .nestedScroll(state.nestedScrollConnection),
    ) {
        content()

        PullToRefreshContainer(
            state = state,
            modifier = Modifier.align(Alignment.TopCenter),
            indicator = indicator
        )
    }
}