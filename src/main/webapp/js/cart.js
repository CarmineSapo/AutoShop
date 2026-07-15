document.addEventListener("DOMContentLoaded", function () {

    const cartPage =
        document.getElementById("cart-page");

    if (cartPage === null) {
        return;
    }

    const contextPath =
        cartPage.dataset.contextPath;

    const checkboxes =
        document.querySelectorAll(
            ".cart-selection-checkbox"
        );

    const selectedCount =
        document.getElementById(
            "selected-count"
        );

    const selectedTotal =
        document.getElementById(
            "selected-total"
        );

    const checkoutButton =
        document.getElementById(
            "checkout-button"
        );

    checkboxes.forEach(function (checkbox) {

        checkbox.addEventListener(
            "change",
            function () {

                const parameters =
                    new URLSearchParams();

                parameters.append(
                    "vehicleId",
                    checkbox.dataset.vehicleId
                );

                parameters.append(
                    "selected",
                    checkbox.checked
                );

                fetch(
                    contextPath
                    + "/cart/change-selection",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/x-www-form-urlencoded"
                        },

                        body: parameters.toString()
                    }
                )
                    .then(function (response) {

                        if (!response.ok) {
                            throw new Error(
                                "Errore nella richiesta"
                            );
                        }

                        return response.json();
                    })

                    .then(function (data) {

                        selectedCount.textContent =
                            data.selectedCount;

                        selectedTotal.textContent =
                            data.selectedTotal.toFixed(2);

                        checkoutButton.disabled =
                            data.selectedCount === 0;
                    })

                    .catch(function () {

                        alert(
                            "Errore durante l'aggiornamento del carrello"
                        );

                        checkbox.checked =
                            !checkbox.checked;
                    });
            }
        );
    });
});