from django.contrib import admin
from .models import Ticket, History

# Register your models here.
admin.site.register(Ticket)
admin.site.register(History)