document.addEventListener('DOMContentLoaded', function() {
    var iconLinks = document.querySelectorAll('.icon-link');
    var activeLinkId = sessionStorage.getItem('activeLinkId');

    function setActiveState(iconLink) {
        var iconSquare = iconLink.querySelector('.icon-square');
        var img = iconSquare.querySelector('img');

        iconLink.classList.add('active');
        img.src = img.dataset.activeSrc;
    }

    function resetAllStates() {
        iconLinks.forEach(function(link) {
            link.classList.remove('active');
            var img = link.querySelector('.icon-square img');
            img.src = img.dataset.originalSrc;
        });
    }

    // Если есть активная кнопка, активируем её
    if (activeLinkId) {
        var activeLink = document.querySelector('#' + activeLinkId);
        if (activeLink) {
            setActiveState(activeLink);
        }
    } else {
        // Если нет активной кнопки, активируем кнопку "Запрос"
        var ticketsLink = document.querySelector('#tickets-link');
        if (ticketsLink) {
            setActiveState(ticketsLink);
            sessionStorage.setItem('activeLinkId', ticketsLink.id);
        }
    }

    iconLinks.forEach(function(iconLink) {
        iconLink.addEventListener('click', function(event) {
            resetAllStates();

            setActiveState(this);

            sessionStorage.setItem('activeLinkId', this.id);

            if (!event.target.closest('.icon-square')) {
                return true;
            }
            event.preventDefault();
            window.location.href = this.href;
        });
    });
});