package com.alexcao.dexpense.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.alexcao.dexpense.presentation.navigation.Route
import com.alexcao.dexpense.presentation.screens.home.widgets.ExpenseDialog
import com.alexcao.dexpense.presentation.screens.home.widgets.ExpensePage
import com.alexcao.dexpense.presentation.screens.home.widgets.HomeHeader
import com.alexcao.dexpense.ui.theme.DexpenseTheme
import java.time.LocalDate

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val state = homeViewModel.state.collectAsState().value
    val selectedPage = state.selectedPage
    val selectedMonth = state.selectedMonth
    val expenses = state.expenses
    val sources = state.sourceInfos
    val categories = state.categories
    var isDialogOpen by rememberSaveable { mutableStateOf(false) }
    var currentDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val pagerState = rememberPagerState(
        initialPage = selectedPage,
        pageCount = { 2 }
    )

    LaunchedEffect(selectedPage) {
        pagerState.animateScrollToPage(selectedPage)
    }

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            homeViewModel.onPageSelected(page)
        }
    }

    Scaffold(
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            HomeHeader(
                selectedMonth = selectedMonth,
                onChangeMonth = { month ->
                    homeViewModel.onMonthSelected(month)
                },
                selectedPage = selectedPage,
                onPageSelected = { page ->
                    homeViewModel.onPageSelected(page)
                },
                onOpenSettings = {
                    navHostController.navigate(Route.SETTINGS.route)
                }
            )
            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> ExpensePage(
                        expenses = expenses,
                        onAddExpense = { date ->
                            isDialogOpen = true
                            currentDate = date
                        }
                    )
                    1 -> ExpensePage(expenses = expenses)
                }
            }
        }

        if (isDialogOpen) {
            ExpenseDialog(
                onDismissRequest = {
                    isDialogOpen = false
                },
                onSave = {
                    homeViewModel.onSaveExpense(it)
                },
                initialExpense = null,
                localDate = currentDate,
                sourceInfos = sources,
                categories = categories
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    val navHostController = rememberNavController()
    DexpenseTheme {
        HomeScreen(
            navHostController = navHostController
        )
    }
}