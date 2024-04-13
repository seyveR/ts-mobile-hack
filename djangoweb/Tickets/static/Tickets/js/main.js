document.addEventListener('DOMContentLoaded', function() {
  var iconLinks = document.querySelectorAll('.icon-link');

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

  var activeLinkId = sessionStorage.getItem('activeLinkId');
  if (activeLinkId) {
      var activeLink = document.querySelector('#' + activeLinkId);
      if (activeLink) {
          setActiveState(activeLink);
      }
  }

  iconLinks.forEach(function(iconLink) {
      var iconSquare = iconLink.querySelector('.icon-square');
      var img = iconSquare.querySelector('img');

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
