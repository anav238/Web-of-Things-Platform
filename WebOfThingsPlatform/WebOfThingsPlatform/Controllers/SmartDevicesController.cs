using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebOfThingsPlatform.Entities;

namespace WebOfThingsPlatform.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class SmartDevicesController : ControllerBase
    {
        private readonly ILogger<SmartDevicesController> _logger;
        public SmartDevicesController(ILogger<SmartDevicesController> logger)
        {
            _logger = logger;
        }

        [HttpGet]
        public Device GetAllDevicess(Guid Id)
        {

            return new Device
            {
                Id = Guid.NewGuid(),
                Title = "title",
                Description = "description",
                //Category = CategoryEnum.Security,
                BaseLink = "www.url.ro",
                Properties = new List<string> { "properties" },
                Actions = new List<string> { "actions" },
                Events = new List<string> { "events" },
                Links = new List<string> { "www.links.ro" }
            };
        }

        [HttpPost("DoAction")]
        public IActionResult ExecuteDeviceAction(Guid Id)
        {
            return Ok("Action executed");
        }
    }
}
