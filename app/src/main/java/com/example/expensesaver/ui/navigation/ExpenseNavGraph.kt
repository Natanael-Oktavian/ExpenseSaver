/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.expensesaver.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.expensesaver.ui.expense.ExpenseEditDestination
import com.example.expensesaver.ui.expense.ExpenseEditScreen
import com.example.expensesaver.ui.home.HomeDestination
import com.example.expensesaver.ui.home.HomeScreen
import com.example.expensesaver.ui.expense.ExpenseEntryDestination
import com.example.expensesaver.ui.expense.ExpenseEntryScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun ExpenseNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(ExpenseEntryDestination.route) },
                navigateToItemUpdate = {
                    navController.navigate("${ExpenseEditDestination.route}/${it}")
                }
            )
        }
        composable(route = ExpenseEntryDestination.route) {
            ExpenseEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ExpenseEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ExpenseEditDestination.itemIdArg) {
                type = NavType.StringType
            })
        ) {
            ExpenseEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
