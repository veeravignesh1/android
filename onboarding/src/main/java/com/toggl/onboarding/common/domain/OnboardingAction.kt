package com.toggl.onboarding.common.domain

import com.toggl.architecture.core.ActionWrapper
import com.toggl.onboarding.login.domain.LoginAction
import com.toggl.onboarding.login.domain.formatForDebug
import com.toggl.onboarding.passwordreset.domain.PasswordResetAction
import com.toggl.onboarding.passwordreset.domain.formatForDebug
import com.toggl.onboarding.signup.domain.SignUpAction
import com.toggl.onboarding.signup.domain.formatForDebug
import com.toggl.onboarding.sso.domain.SsoAction
import com.toggl.onboarding.sso.domain.formatForDebug
import com.toggl.onboarding.welcome.domain.WelcomeAction
import com.toggl.onboarding.welcome.domain.formatForDebug

sealed class OnboardingAction {
    class Welcome(override val action: WelcomeAction) : OnboardingAction(), ActionWrapper<WelcomeAction>
    class Login(override val action: LoginAction) : OnboardingAction(), ActionWrapper<LoginAction>
    class SignUp(override val action: SignUpAction) : OnboardingAction(), ActionWrapper<SignUpAction>
    class PasswordReset(override val action: PasswordResetAction) : OnboardingAction(), ActionWrapper<PasswordResetAction>
    class Sso(override val action: SsoAction) : OnboardingAction(), ActionWrapper<SsoAction>
}

fun OnboardingAction.formatForDebug(): String =
    when (this) {
        is OnboardingAction.Login -> this.action.formatForDebug()
        is OnboardingAction.Welcome -> this.action.formatForDebug()
        is OnboardingAction.SignUp -> this.action.formatForDebug()
        is OnboardingAction.PasswordReset -> this.action.formatForDebug()
        is OnboardingAction.Sso -> this.action.formatForDebug()
    }
