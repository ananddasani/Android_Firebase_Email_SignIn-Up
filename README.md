# Android_Firebase_Email_SignIn-Up
App with SignIn, SignUp, Logout, Delete Account features

## SignUp.java
```
EditText fullName, email, password, confPassword;
Button signupButton;

String userFullName, userEmail, userPassword, userConfirmPass;

FirebaseAuth auth;

fullName = findViewById(R.id.editTextTextFullName);
email = findViewById(R.id.editTextTextEmailAddress);
password = findViewById(R.id.editTextTextPass);
confPassword = findViewById(R.id.editTextTextConfirmPass);
signupButton = findViewById(R.id.signUpButton);

auth = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userFullName = fullName.getText().toString();
                userEmail = email.getText().toString();
                userPassword = password.getText().toString();
                userConfirmPass = confPassword.getText().toString();

                //chek the info given
                if (userFullName.equals("")) {
                    fullName.setError("Full Name Required");
                    return;
                }

                if (userEmail.equals("")) {
                    email.setError("Email Required");
                    return;
                }

                if (userPassword.equals("")) {
                    password.setError("Password Required");
                    return;
                }

                if (userConfirmPass.equals("")) {
                    confPassword.setError("Confirm Password Required");
                    return;
                }

                //all info is given check for confirm password is same or not
                if (!userPassword.equals(userConfirmPass)) {
                    confPassword.setError("Confirm Password Doesn't Match");
                    return;
                }

                //all ok, signup the user
                auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUp.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this, "User Registration Failed :: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
```

## SignIn.java
```
EditText email, password;
TextView newUser;
Button signButton;

String userEmail, userPass;

FirebaseAuth auth;

auth = FirebaseAuth.getInstance();

email = findViewById(R.id.editTextTextEmail);
password = findViewById(R.id.editTextTextPassword);
signButton = findViewById(R.id.signInButton);
newUser = findViewById(R.id.newUserTextView);

        //new user textview clicked
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
                finish();
            }
        });

        //sign button clicked
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate the email, password inserted or not
                userEmail = email.getText().toString();
                userPass = password.getText().toString();

                if (userEmail.equals("")) {
                    email.setError("Email is required");
                    return;
                }

                if (userPass.equals("")) {
                    password.setError("Password is required");
                    return;
                }

                //Email password given sign in the user
                auth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //user entered correct credential move to home
                            Toast.makeText(SignIn.this, "user verified", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignIn.this, MainActivity.class));
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignIn.this, "Invalid Credential :: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
```

```
    /*
    if user is already logged in no need to ask them again to login
     */
    @Override
    protected void onStart() {

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Toast.makeText(this, "You are already registered :)", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignIn.this, MainActivity.class));
            finish();
        }

        super.onStart();
    }
```

## MainActivity.java
```
Button logoutButton, deleteUserButton;

FirebaseUser user;

logoutButton = findViewById(R.id.logoutButton);
deleteUserButton = findViewById(R.id.deleteButton);

user = FirebaseAuth.getInstance().getCurrentUser();

logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, SignIn.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "User is NUll...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Yourself!")
                        .setMessage("Are you sure you want to deregister yourself?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                //prepare the progress dialog
                                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                                progressDialog.setMessage("Deleting user");
                                progressDialog.setTitle("Please Wait!");
                                progressDialog.show();

                                //delete the user
                                FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(MainActivity.this, "User Deleted", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                        startActivity(new Intent(MainActivity.this, SignIn.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, "User Doesn't Exist", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });


```
