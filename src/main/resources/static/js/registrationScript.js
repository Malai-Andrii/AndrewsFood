var tempCalory = " ";
var proteinsCoef = "1";
var lipidsCoef = "1";
var carboCoef = "4";
var sugarsCoef = "10";

function triggerSportDisplay() {
    $(function() {
        if ( $("#nutritionStyle").val() == 'sport') {
            $("#bodyConstitutionDiv").show();
            $("#trainTypeDiv").show();
            $("#sedentary").hide();
            $("#moderate").hide();
            $("#lifeStyle").val('medium');
        }
        else {
            $("#sedentary").show();
            $("#moderate").show();
            $("#bodyConstitutionDiv").hide();
            $("#trainTypeDiv").hide();
        }
        return false;
    })
}
function triggerLifeDisplay() {
    $(function() {
        if ($("#lifeStyle").val() == 'sportive') {
            $("#usual").hide() ;
            $("#vegetarian").hide() ;
            $("#vegan").hide();
            $("#redutarian").hide();
            $("#nutritionStyle").val('sport');
        }
        else {
            $("#usual").show(); ;
            $("#vegetarian").show(); ;
            $("#vegan").show(); ;
            $("#redutarian").show(); ;
        }
        return false;
    })
}
function triggerCoef() {
    $(function() {
        if ($("#proteinsCoef").attr('readonly') == 'readonly') {
            $("#proteinsCoef").removeAttr('readonly');
            $("#lipidsCoef").removeAttr('readonly');
            $("#carboCoef").removeAttr('readonly');
            $("#sugarsCoef").removeAttr('readonly');

            $("#proteinsCoef").val(proteinsCoef);
            $("#lipidsCoef").val(lipidsCoef);
            $("#carboCoef").val(carboCoef);
            $("#sugarsCoef").val(sugarsCoef);
        }
        else {
            $("#proteinsCoef").attr("readonly", "readonly");
            $("#lipidsCoef").attr("readonly", "readonly");
            $("#carboCoef").attr("readonly", "readonly");
            $("#sugarsCoef").attr("readonly", "readonly");

            proteinsCoef = $("#proteinsCoef").val();
            lipidsCoef = $("#lipidsCoef").val();
            carboCoef = $("#carboCoef").val();
            sugarsCoef =  $("#sugarsCoef").val();

            $("#proteinsCoef").val("");
            $("#lipidsCoef").val("");
            $("#carboCoef").val("");
            $("#sugarsCoef").val("");
        }
        return false;
    })
}

function PLCandCalority() {
    generatePLC();
    generateCalority();
    return false;
}

function IMTandCalority() {
    changeIMT();
    generateCalority();
    return false;
}

function checkValue(id) {
    $(function() {
        var min = Number.parseInt($("#" + id).attr('min'));
        var max = Number.parseInt($("#" + id).attr('max'));
        var currentValue = Number.parseInt($("#" + id).val());

        if (currentValue >= min && currentValue <= max) {return false;}
        else if (min > currentValue) {$("#" + id).val(min)}
        else if (max < currentValue) {$("#" + id).val(max)}
        return false;
    })
}

function setPLC(p, l, c, s) {
    $(function() {
        $("#proteinsCoef").val(p);
        $("#lipidsCoef").val(l);
        $("#carboCoef").val(c);
        $("#sugarsCoef").val(s);
    })
}

function generatePLC() {
    $(function() {
        var nutritionStyle = $("#nutritionStyle").val();
        var trainType = $("#trainType").val();
        var bodyConstitution = $("#bodyConstitution").val();
        var imtCoef = $("#imtLegend").html();
        var sexCoef = ($("#sex").val() == "male") ? 0 : 0.5;
        imtCoef = imtCoef.substring(imtCoef.length - imtCoef.lastIndexOf(':') - 1);


        console.log(nutritionStyle);
        console.log(bodyConstitution);
        console.log(trainType);

        console.log(nutritionStyle == "usual");
        console.log(trainType == "power");
        console.log(bodyConstitution == "ectomorph");

        if (nutritionStyle == "usual") {setPLC(1, 1, 4, 10);}
        else if (nutritionStyle == "sport") {
            if (trainType == "power") {
                if (bodyConstitution == "ectomorph") {setPLC(2, 1, 6, 15);}
                else if (bodyConstitution == "mezomorph") {setPLC(2, 1, 5, 10);}
                else if (bodyConstitution == "endomorph") {setPLC(2, 1, 4, 10);}
            }
            else if (trainType == "cardio") {
                if (bodyConstitution == "ectomorph") {setPLC(1, 1, 6, 15);}
                else if (bodyConstitution == "mezomorph") {setPLC(1, 1, 5, 10);}
                else if (bodyConstitution == "endomorph") {setPLC(1, 1, 4, 10);}
            }
            else if (trainType == "mixed") {
                if (bodyConstitution == "ectomorph") {setPLC(2, 1, 6, 15);}
                else if (bodyConstitution == "mezomorph") {setPLC(2, 1, 5, 10);}
                else if (bodyConstitution == "endomorph") {setPLC(2, 1, 4, 10);}
            }
        }
        // if (imtCoef < 30 - sexCoef) {return false;}
        // else {setPLC(2, 1, 4, 10);}
        return false;
    })
}

function generateCalority() {
    $(function() {
        if ($("#calorityAuto").prop('checked')) {
            var AMR, BMR, W, H, A, imtCoef, sexCoef;
            var IMT = 0;
            if (($("#weight").val() > 30 && $("#weight").val() < 250)
                &&
                ($("#height").val() > 50 && $("#height").val() < 250)
                &&
                ($("#age").val() > 16 && $("#age").val() < 120)) {
                W = $("#weight").val();
                H = $("#height").val();
                if ($("#age").val() < 20) {
                    A = 20;
                }
                else {A = $("#age").val();}
            }
            else {
                $("#calority").attr('placeholder', 'некоректні параметри')
                return false;
            }
            sexCoef = ($("#sex").val() == "male") ? 0 : 0.5;
            if (sexCoef == 0) {
                BMR = 88.362 + (13.397 * W) + (4.799 * H) - (5.677 * A)
            }
            else {
                BMR = 447.593 + (9.247 * W) + (3.098  * H) - (4.330 * A)
            }
            switch ($("#lifeStyle").val()) {
                case "sedentary": AMR = 1.2; break;
                case "moderate": AMR = 1.375; break;
                case "medium": AMR = 1.55; break;
                case "active" : AMR = 1.725; break;
                case "sportive": AMR = 1.9; break;
            }
            imtCoef = $("#imtLegend").html();
            imtCoef = imtCoef.substring(imtCoef.length - imtCoef.lastIndexOf(':') - 1);
            if (imtCoef < 18.5 - sexCoef) {
                IMT = 0.1;
            }
            else if ( imtCoef > 25 - sexCoef && imtCoef < 35 - sexCoef) {
                IMT = -0.1;
            }
            else if (imtCoef > 35 - sexCoef && imtCoef < 40 - sexCoef) {
                IMT = -0.2;
            }
            else if (imtCoef > 40 - sexCoef) {
                IMT = -0.3;
            }
            var calorityResult = AMR * BMR;
            calorityResult += calorityResult * IMT;
            $("#calority").val(calorityResult.toFixed(0));
        }
        return false;
    })
}

function changeIMT() {
    $(function() {
        var height = $("#height").val();
        var sexCoef = ($("#sex").val() == "male") ? 0 : 0.5;
        var weight = $("#weight").val();
        if (height == "" || weight == "") imt = 0;
        else var imt = (weight / Math.pow((height / 100), 2)).toFixed(1);

        $("#imtLegend").html("ІМТ: " + imt);

        if (imt == 0) {
            $("#imtLegend").css("color", "red");
            $("#imtText").html("Введіть всі необхідні параметри та перевірте їх коректність.");
        }
        else if (imt < 18.5 - sexCoef) {
            $("#imtLegend").css("color", "red");
            $("#imtText").html("Ваша маса тіла недостатня. "
                + "При автоматичному визначенні система додасть до вашого раціону ще 10% калорій");
        }
        else if (imt < 25 - sexCoef) {
            $("#imtLegend").css("color", "green");
            $("#imtText").html("Ваша маса тіла в нормі. Так тримати!");
        }
        else if (imt < 30 - sexCoef) {
            $("#imtLegend").css("color", "rgb(241, 186, 2)");
            $("#imtText").html("У Вас помірна гладкість. " +
                "При автоматичному визначенні система забере 10% калорій, щоб Ви трохи схудли.");
        }
        else if (imt < 35 - sexCoef) {
            $("#imtLegend").css("color", "red");
            $("#imtText").html("У Вас ожиріння І ступеню. " +
                "При автоматичному визначенні система забере 10% калорій, а також пропонує їсти більше білків замість жирів та вуглеводів, " +
                "Це дозволить не втрачати м'язову масу разом із жиром. " +
                "Також бажано відмовитися від смаженого та засилля солодощів в раціоні.");
        }
        else if (imt < 40 - sexCoef) {
            $("#imtLegend").css("color", "red");
            $("#imtText").html("У Вас ожиріння ІІ ступеню. " +
                "При автоматичному визначенні система забере 20% калорій, а також пропонує їсти більше білків замість жирів та вуглеводів. " +
                "Це дозволить не втрачати м'язову масу разом із жиром. " +
                "Наполягаємо відмовитися від смаженого та засилля солодощів в раціоні.");
        }
        else {
            $("#imtLegend").css("color", "red");
            $("#imtText").html("У Вас ожиріння ІІІ ступеню. " +
                "При автоматичному визначенні система забере 30% калорій. Відмовтеся від солодкого та смаженого. " +
                "Але обов'язково зверніть увагу, що це серйозний хворобливий стан, тому найкраще, " +
                "щоб Вам сформував раціон лікар.");
        }
        return false;
    })
}
function triggerCalority() {
    $(function() {
        if ($("#calority").attr('readonly') == 'readonly') {
            $("#calority").removeAttr('readonly');
            $("#calority").removeAttr('placeholder');
        }
        else {
            $("#calority").attr('readonly', 'readonly');
            generateCalority();
        }
        return false;
    })
}

function checkPass(password) {
    $(function() {
        var pass = password;
        if (pass.length < 6) {
            $("#passwordFeedback").text("Пароль повинен містити принаймі 6 символів.");
            $("#password").removeClass('is-valid');
            $("#password").addClass('is-invalid');
            return false;
        }
        else if(pass === pass.toLowerCase()) {
            $("#passwordFeedback").text("Пароль повинен містити хоч одну маленьку літеру");
            $("#password").removeClass('is-valid');
            $("#password").addClass('is-invalid');
            return false;
        }
        else if(pass === pass.toUpperCase()) {
            $("#passwordFeedback").text("Пароль повинен містити хоч одну маленьку літеру");
            $("#password").removeClass('is-valid');
            $("#password").addClass('is-invalid');
            return false;
        }
        else if(!(/\d+/g.test(pass))) {
            $("#passwordFeedback").text("Пароль повинен містити хоч одну цифру");
            $("#password").removeClass('is-valid');
            $("#password").addClass('is-invalid');
            return false;
        }
        else {
            $("#password").removeClass('is-invalid');
            $("#password").addClass('is-valid');
            return false;
        }
    })
}

function checkPassConfirm(confirm) {
    $(function() {
        if ($("#password").val() === confirm) {
            $("#passwordConfirm").removeClass('is-invalid');
            $("#passwordConfirm").addClass('is-valid');
        }
        else {
            $("#passwordConfirm").removeClass('is-valid');
            $("#passwordConfirm").addClass('is-invalid');
        }
        return false;
    })
}

function checkMail(mail) {
    $(function() {
        if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(mail.trim())) {
            $("#email").removeClass('is-invalid');
            $("#email").addClass('is-valid');
        }
        else {
            $("#email").removeClass('is-valid');
            $("#email").addClass('is-invalid');
        }
        return false;
    })
}