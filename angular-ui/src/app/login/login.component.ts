import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  imports: [ReactiveFormsModule, NgIf],
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  activeTab: 'customer' | 'admin' = 'customer';
  isSignUp: boolean = false;

  customerForm: FormGroup;
  adminForm: FormGroup;
  signUpForm: FormGroup;

  constructor(private fb: FormBuilder, private router: Router) {
    this.customerForm = this.fb.group({
      username: [''],
    });

    this.adminForm = this.fb.group({
      username: [''],
      password: [''],
    });

    this.signUpForm = this.fb.group({
      username: [''],
      email: [''],
      isVIP: [false],
    });
  }

  switchTab(tab: 'customer' | 'admin') {
    this.activeTab = tab;
    this.isSignUp = false; // Reset to login view when switching tabs
  }

  toggleSignUp() {
    this.isSignUp = !this.isSignUp;
  }

  onSubmitCustomer() {
    console.log('Customer Form Data:', this.customerForm.value);
    this.router.navigate(['/home']);
  }

  onSubmitAdmin() {
    console.log('Admin Form Data:', this.adminForm.value);
    this.router.navigate(['/admin']);
  }

  onSubmitSignUp() {
    console.log('Sign Up Form Data:', this.signUpForm.value);
    this.router.navigate(["/login"]);
  }
}
