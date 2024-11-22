import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { Router } from '@angular/router';
import { ApiService } from '../api.service'; // Service for API calls

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

  constructor(private fb: FormBuilder, private router: Router, private apiService: ApiService) {
    this.customerForm = this.fb.group({
      email: [''] // Only email for login
    });

    this.adminForm = this.fb.group({
      username: [''],
      password: [''] // Admin login requires both username and password
    });

    this.signUpForm = this.fb.group({
      name: [''],
      email: [''], // Only username and email for sign-up
      vip: [false]
    });
  }

  switchTab(tab: 'customer' | 'admin') {
    this.activeTab = tab;
    this.isSignUp = false; // Reset to login view when switching tabs

    // Clear all form fields and validations when switching tabs
    if (tab === 'customer') {
      this.customerForm.reset();
    } else if (tab === 'admin') {
      this.adminForm.reset();
    }
  }

  toggleSignUp() {
    this.isSignUp = !this.isSignUp;

    if (this.isSignUp) {
      this.signUpForm.reset(); // Reset signup form when toggling
    }
  }

  onSubmitCustomer() {
    if (this.customerForm.valid) {
      const email = this.customerForm.value.email;

      this.apiService.getCustomerByEmail(email).subscribe({
        next: (response) => {
          console.log('Logged in successfully:', response);
          alert(`Welcome back, ${response.name || 'Customer'}!`);
          localStorage.setItem('customer', JSON.stringify(response));
          this.router.navigate(['/home']);
        },
        error: (err) => {
          console.error('Login error:', err);
          alert('Login failed. Please check your email and try again.');
        }
      });
    } else {
      alert('Please enter a valid email.');
    }
  }


  onSubmitAdmin() {
    if (this.adminForm.valid) {
      console.log('Admin Form Data:', this.adminForm.value);
      this.router.navigate(['/admin']);
    } else {
      alert('Please fill in all fields.');
    }
  }

  onSubmitSignUp() {
    if (this.signUpForm.valid) {
      const customerData = this.signUpForm.value; // Ensure this includes name, email, and isVip
      this.apiService.createCustomer(customerData).subscribe({
        next: (response) => {
          console.log('Customer signed up successfully:', response);
          alert('Sign up successful!');
          this.signUpForm.reset();
          console.log(this.signUpForm.value);
        },
        error: (err) => {
          console.error('Sign Up error:', err);
          alert('Failed to sign up. Please try again.');
        }
      });
    } else {
      alert('Please fill in all required fields.');
    }
  }

}
