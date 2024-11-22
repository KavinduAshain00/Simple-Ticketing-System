import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
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
      email: ['']
    });
  }

  // Switch between tabs (customer, admin, or vendor)
  switchTab(tab: 'customer' | 'admin' | 'vendor') {
    this.activeTab = tab;
    this.isSignUp = false; // Reset to login view when switching tabs
    this.isVendorSignUp = false;  // Reset vendor sign-up when switching tabs

    this.resetForms();

    if (tab === 'customer') {
      this.addCustomerValidators();
    } else if (tab === 'admin') {
      this.addAdminValidators();
    } else if (tab === 'vendor') {
      this.addVendorValidators();
    }
  }

  // Add validators for customer form
  addCustomerValidators() {
    this.customerForm.get('email')?.setValidators([Validators.required, Validators.email]);
    this.customerForm.get('email')?.updateValueAndValidity();
  }

  // Add validators for admin form
  addAdminValidators() {
    this.adminForm.get('username')?.setValidators([Validators.required]);
    this.adminForm.get('password')?.setValidators([Validators.required]);
    this.adminForm.get('username')?.updateValueAndValidity();
    this.adminForm.get('password')?.updateValueAndValidity();
  }

  // Add validators for vendor form
  addVendorValidators() {
    this.vendorForm.get('email')?.setValidators([Validators.required, Validators.email]);
    this.vendorForm.get('email')?.updateValueAndValidity();
  }

  addVendorSignupValidators() {
    this.vendorSignUpForm.get('name')?.setValidators([Validators.required, Validators.email]);
    this.vendorSignUpForm.get('email')?.setValidators([Validators.required, Validators.email]);
    this.vendorSignUpForm.get('name')?.updateValueAndValidity();
    this.vendorSignUpForm.get('email')?.updateValueAndValidity();
  }

  addSignUpValidators(){
    this.signUpForm.get('name')?.setValidators([Validators.required]); // Name is required
  this.signUpForm.get('email')?.setValidators([Validators.required, Validators.email]); // Email is required and must be valid
  this.signUpForm.get('vip')?.setValidators([]); // No specific validators for vip
  this.signUpForm.get('name')?.updateValueAndValidity();
  this.signUpForm.get('email')?.updateValueAndValidity();
  this.signUpForm.get('vip')?.updateValueAndValidity();
  }

  // Reset all forms to clear validators and values
  resetForms() {
    this.customerForm.reset();
    this.adminForm.reset();
    this.signUpForm.reset();
    this.vendorForm.reset();
    this.vendorSignUpForm.reset();

    // Clear validators for all forms
    this.customerForm.get('email')?.clearValidators();
    this.adminForm.get('username')?.clearValidators();
    this.adminForm.get('password')?.clearValidators();
    this.vendorForm.get('email')?.clearValidators();
    this.vendorSignUpForm.get('name')?.clearValidators();
    this.vendorSignUpForm.get('email')?.clearValidators();
    this.signUpForm.get('name')?.clearValidators();
    this.signUpForm.get('email')?.clearValidators();
    this.signUpForm.get('vip')?.clearValidators();
  }

  // Toggle between customer login and sign-up
  toggleSignUp() {
    this.isSignUp = !this.isSignUp;

    if (this.isSignUp) {
      this.signUpForm.reset(); // Reset signup form when toggling
      this.addSignUpValidators();
    }
  }

  // Toggle between vendor login and sign-up
  toggleVendorSignUp() {
    this.isVendorSignUp = !this.isVendorSignUp;

    if (this.isVendorSignUp) {
      this.vendorSignUpForm.reset(); // Reset vendor signup form when toggling
      this.addVendorSignupValidators();
    }
  }

  // Handle vendor login
  onSubmitVendor() {
    if (this.vendorForm.valid) {
      const email = this.vendorForm.value.email;

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
    if (this.vendorSignUpForm.valid) {
      const vendorData = this.vendorSignUpForm.value;

      this.apiService.createVendor(vendorData).subscribe({
        next: (response) => {
          console.log('Vendor signed up successfully:', response);
          alert('Vendor sign-up successful!');
          this.vendorSignUpForm.reset(); // Reset the form after successful sign-up
        },
        error: (err) => {
          console.error('Vendor sign-up error:', err);
          alert('Vendor sign-up failed. Please try again.');
        },
      });
    } else {
      alert('Please fill in all required fields.blah');
    }
  }
}
