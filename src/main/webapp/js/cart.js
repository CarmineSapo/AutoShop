document.addEventListener("DOMContentLoaded", () => {

    const cartPage = document.getElementById("cart-page");

    if( cartPage === null){
        return;
    }

    const contextPath = cartPage.dataset.contextPath;

    const checkboxes = document.querySelectorAll( ".cart-selection-checkbox");

    const selectedCountElement = document.getElementById("selected-count");

    const selectedTotalElement = document.getElementById("selected-total");

    const checkoutButton = document.getElementById("checkout-button");

    function updateCheckoutButton(){

        const selectedCount = Number(selectedCountElement.textContent);

        checkoutButton.disabled = selectedCount === 0;
    }

    checkboxes.forEach( (checkbox) => {

        checkbox.addEventListener("change", async () =>{

            /*
                * Conserviamo il valore precedente.
                * Se la richiesta fallisce, ripristiniamo
                * graficamente la checkbox.
                */

            const  previusValue = !checkbox.checked;

            checkbox.disabled = true;

            const parameters = new URLSearchParams();

            parameters.append( "vehicleId", checkbox.dataset.vehicleId);

            parameters.append ( "selected", String (checkbox.checked));

            try {
                const response = await fetch(
                    contextPath + "/cart/change-selection",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
                        },

                        body: parameters.toString()
                    }
                );

                const data = await response.json();

                if ( !response.ok || data.success !== true){

                    throw new Error( data.message || "Errore durante l'aggiornamento");
                }

                selectedCountElement.textContent = data.selectedCount;

                selectedTotalElement.textContent = Number(
                    data.selectedTotal).toLocaleString(
                        "it-IT",
                    {
                        minimumFractionDigits: 2,
                        maximumFractionDigits: 2
                    }
                );

                updateCheckoutButton();

            } catch (error){

                checkbox.checked = previusValue;

                alert( "Non è stato possibile aggiornare il carrello.");

            } finally {
                checkbox.disabled = false;
            }
        });
    });

    updateCheckoutButton();
})