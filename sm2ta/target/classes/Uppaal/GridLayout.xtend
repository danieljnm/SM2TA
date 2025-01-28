package Uppaal

class GridLayout {
	int startX = 0
	int startY = 0
	int horizontalSpacing = 400
	int verticalSpacing = 150
	int maxPerRow = 4
	
	def applyLayout(Template template) {
		template.locations.forEach[location, index |
			val row = index / maxPerRow
			val col = index % maxPerRow
			
			location.x = startX + (col * horizontalSpacing)
			location.y = startY + (row * verticalSpacing)
			
			if (location.name !== null) {
				location.name.x = location.x - 15
				location.name.y = location.y + 15
			}
			
			if (location.label !== null) {
				location.label.x = location.x - 15
				location.label.y = location.y + 30
			}
		]
		
		template.applyTransitions
	}
	
	def applyTransitions(Template template) {
		template.transitions.forEach[transition |
			val source = template.locations.findFirst[it.id == transition.source]
            val target = template.locations.findFirst[it.id == transition.target]
            
            if (source === null || target === null) {
            	return
            }
            
            val midX = (source.x + target.x) / 2
            val midY = (source.y + target.y) / 2
            transition.labels.forEach [ label, index |
                label.x = midX
                label.y = midY + (index * 15)
            ]
		]
	}
}