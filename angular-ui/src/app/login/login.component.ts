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
  activeTab: 'customer' | 'admin' | 'vendor' = 'customer';
  isSignUp: boolean = false;
  isVendorSignUp: boolean = false;  // Added for vendor sign-up toggle

  customerForm: FormGroup;
  adminForm: FormGroup;
  signUpForm: FormGroup;
  vendorForm: FormGroup;
  vendorSignUpForm: FormGroup; // Added form for vendor sign-up

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

    this.vendorForm = this.fb.group({
      name: [''],
    });

    this.vendorSignUpForm = this.fb.group({
      name: [''],
      email: [''],
      vendorCode: ['']
    });
  }

  // Switch between tabs (customer, admin, or vendor)
  switchTab(tab: 'customer' | 'admin' | 'vendor') {
    this.activeTab = tab;
    this.isSignUp = false; // Reset to login view when switching tabs
    this.isVendorSignUp = false;  // Reset vendor sign-up when switching tabs

    // Clear all form fields and validations when switching tabs
    if (tab === 'customer') {
      this.customerForm.reset();
    } else if (tab === 'admin') {
      this.adminForm.reset();
    } else if (tab === 'vendor') {
      this.vendorForm.reset();
      this.vendorSignUpForm.reset();
    }
  }

  // Toggle between customer login and sign-up
  toggleSignUp() {
    this.isSignUp = !this.isSignUp;

    if (this.isSignUp) {
      this.signUpForm.reset(); // Reset signup form when toggling
    }
  }

  // Toggle between vendor login and sign-up
  toggleVendorSignUp() {
    this.isVendorSignUp = !this.isVendorSignUp;

    if (this.isVendorSignUp) {
      this.vendorSignUpForm.reset(); // Reset vendor signup form when toggling
    }
  }

  // Handle vendor login
  onSubmitVendor() {
    // Vendor login logic here
    console.log('Vendor login data:', this.vendorForm.value);
  }

  // Handle customer login
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

  // Handle admin login
  onSubmitAdmin() {
    if (this.adminForm.valid) {
      console.log('Admin Form Data:', this.adminForm.value);
      this.router.navigate(['/admin']);
    } else {
      alert('Please fill in all fields.');
    }
  }

  // Handle customer sign-up
  onSubmitSignUp() {
    if (this.signUpForm.valid) {
      const customerData = this.signUpForm.value; // Ensure this includes name, email, and isVip
      this.apiService.createCustomer(customerData).subscribe({
        next: (response) => {
          console.log('Customer signed up successfully:', response);
          alert('Sign up successful!');
          this.signUpForm.reset();
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

  // Handle vendor sign-up
  onSubmitVendorSignUp() {

  }
}
