  // Validate phone number to ensure it's exactly 10 digits
function validateRole(input) {
    const validRoles = ['user', 'admin','USER','ADMIN'];


    if (validRoles.includes(input)) {
        document.getElementById('roleError').style.display = 'none';
        return true;
    } else {
        document.getElementById('roleError').style.display = 'block';
        return false;
    }
}

    function validatePhoneNumber(input) {
      if (phoneNumber.value.length === 12 && phoneNumber.value.startsWith('91')) {
    phoneNumber.value = phoneNumber.value.slice(2);
}


  const patt = /^[0]?[6789]\d{9}$/;

    if (patt.test(phoneNumber.value)) {
    document.getElementById('phoneError').style.display = 'none';
        return true; // Valid phone number


        }
         else if(phoneNumber.value.length<10){
          document.getElementById('phoneError').style.display = 'block';
         document.getElementById('phoneError').textContent='Please make sure mobile number is 10 digit ';
          return false;

         }
         else {
         document.getElementById('phoneError').style.display = 'block';
         document.getElementById('phoneError').textContent='Please make sure mobile num is beginning with 9,8,7 or 6 and does not contain alphabets';
         return false;

        }
    }

    // Validate email to match standard email format
    function validateEmail(input) {
        const emailRegex = /^[a-zA-Z0-9_.+\-]+[\x40][a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}$/ ;
        if (!emailRegex.test(input)) {
         document.getElementById('emailError').style.display = 'block';
         return false;
        } else {
            document.getElementById('emailError').style.display = 'none';
            return true;
        }
    }

  function validateName(name) {
  // Regular expression to allow only letters and spaces
  const nameRegex = /^[a-zA-Z\s]{2,50}$/;

  if (!name) {
    return "Name field cannot be empty.";
  }

  if (!nameRegex.test(name)) {
    return "Name must contain only letters and spaces, and be 2 to 50 characters long.";
  }

   return "Valid"; // The name is valid
}

    document.getElementById("registrationForm").addEventListener("submit", function (event) {
            const phoneInput = document.getElementById("phoneNumber");
            const name = document.getElementById("name").value;
            const emailInput = document.getElementById("email").value;

            var validationMessage =validateName(name);
            // Prevent form submission if any input is invalid
    if(  validateEmail(emailInput) && validatePhoneNumber(phoneInput)){
    return true;
    }
    else
    {event.preventDefault();
    }

       if (validationMessage === "Valid") {
       document.getElementById('nameError').style.display = 'hidden';
        return true;
    } else {
       document.getElementById('nameError').style.display = 'inline';
        return false;
   }
        });

